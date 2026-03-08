package com.toyprojects.card_pilot.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.UpdateAvailability
import com.toyprojects.card_pilot.domain.repository.SettingsRepository
import com.toyprojects.card_pilot.model.ThemeType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsRepositoryImpl(private val context: Context) : SettingsRepository {

    private val themeKey = stringPreferencesKey("theme_type")
    private val notiReceiveEnabledKey = booleanPreferencesKey("noti_receive_enabled")
    private val notiReceiveAppsKey = stringSetPreferencesKey("noti_receive_apps")
    private val customAddedAppsKey = stringSetPreferencesKey("custom_added_apps")
    private val keepSelectedCardKey = booleanPreferencesKey("keep_selected_card")
    private val lastViewedCardIdKey = longPreferencesKey("last_viewed_card_id")

    private val appUpdateManager = AppUpdateManagerFactory.create(context)

    override val themeType: Flow<ThemeType> = context.dataStore.data.map { preferences ->
        val themeName = preferences[themeKey]
        ThemeType.entries.find { it.name == themeName } ?: ThemeType.PURPLE
    }

    override val notiReceiveEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[notiReceiveEnabledKey] ?: false
    }

    override val notiReceiveApps: Flow<Set<String>> = context.dataStore.data.map { preferences ->
        preferences[notiReceiveAppsKey] ?: emptySet()
    }

    override val customAddedApps: Flow<Set<String>> = context.dataStore.data.map { preferences ->
        preferences[customAddedAppsKey] ?: emptySet()
    }

    override val keepSelectedCard: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[keepSelectedCardKey] ?: false
    }

    override val lastViewedCardId: Flow<Long?> = context.dataStore.data.map { preferences ->
        preferences[lastViewedCardIdKey]
    }

    override suspend fun setTheme(themeType: ThemeType) {
        context.dataStore.edit { preferences ->
            preferences[themeKey] = themeType.name
        }
    }

    override suspend fun setNotiReceiveEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[notiReceiveEnabledKey] = enabled
        }
    }

    override suspend fun setNotiReceiveApps(apps: Set<String>) {
        context.dataStore.edit { preferences ->
            preferences[notiReceiveAppsKey] = apps
        }
    }

    override suspend fun setCustomAddedApps(apps: Set<String>) {
        context.dataStore.edit { preferences ->
            preferences[customAddedAppsKey] = apps
        }
    }

    override suspend fun setKeepSelectedCard(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[keepSelectedCardKey] = value
        }
    }

    override suspend fun setLastViewedCardId(id: Long) {
        context.dataStore.edit { preferences ->
            preferences[lastViewedCardIdKey] = id
        }
    }

    override suspend fun clearPreferences() {
        withContext(Dispatchers.IO) {
            context.dataStore.edit { it.clear() }
        }
    }

    override suspend fun checkForUpdate(): Boolean = suspendCoroutine { co ->
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            co.resume(appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE)
        }.addOnFailureListener {
            co.resume(false)
        }
    }
}
