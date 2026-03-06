package com.toyprojects.card_pilot.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.toyprojects.card_pilot.domain.repository.SettingsRepository
import com.toyprojects.card_pilot.model.ThemeType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsRepositoryImpl(private val context: Context) : SettingsRepository {

    private val themeKey = stringPreferencesKey("theme_type")
    private val keepSelectedCardKey = booleanPreferencesKey("keep_selected_card")
    private val lastViewedCardIdKey = longPreferencesKey("last_viewed_card_id")

    override val themeType: Flow<ThemeType> = context.dataStore.data.map { preferences ->
        val themeName = preferences[themeKey]
        ThemeType.entries.find { it.name == themeName } ?: ThemeType.PURPLE
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
}
