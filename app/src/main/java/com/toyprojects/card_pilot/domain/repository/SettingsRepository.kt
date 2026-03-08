package com.toyprojects.card_pilot.domain.repository

import com.toyprojects.card_pilot.model.ThemeType
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val themeType: Flow<ThemeType>
    val notiReceiveEnabled: Flow<Boolean>
    val notiReceiveApps: Flow<Set<String>>
    val customAddedApps: Flow<Set<String>>
    val keepSelectedCard: Flow<Boolean>
    val lastViewedCardId: Flow<Long?>

    suspend fun setTheme(themeType: ThemeType)
    suspend fun setNotiReceiveEnabled(enabled: Boolean)
    suspend fun setNotiReceiveApps(apps: Set<String>)
    suspend fun setCustomAddedApps(apps: Set<String>)
    suspend fun setKeepSelectedCard(value: Boolean)
    suspend fun setLastViewedCardId(id: Long)
    suspend fun clearPreferences()
    suspend fun checkForUpdate(): Boolean
}
