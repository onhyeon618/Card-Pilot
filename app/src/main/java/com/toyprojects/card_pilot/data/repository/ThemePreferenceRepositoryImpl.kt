package com.toyprojects.card_pilot.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.toyprojects.card_pilot.domain.repository.ThemePreferenceRepository
import com.toyprojects.card_pilot.model.ThemeType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ThemePreferenceRepositoryImpl(private val context: Context) : ThemePreferenceRepository {

    private val themeKey = stringPreferencesKey("theme_type")

    override val themeType: Flow<ThemeType> = context.dataStore.data.map { preferences ->
        val themeName = preferences[themeKey]
        ThemeType.entries.find { it.name == themeName } ?: ThemeType.PURPLE
    }

    override suspend fun setTheme(themeType: ThemeType) {
        context.dataStore.edit { preferences ->
            preferences[themeKey] = themeType.name
        }
    }
}
