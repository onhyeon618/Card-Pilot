package com.toyprojects.card_pilot.domain.repository

import com.toyprojects.card_pilot.model.ThemeType
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val themeType: Flow<ThemeType>
    val keepSelectedCard: Flow<Boolean>
    val lastViewedCardId: Flow<Long?>

    suspend fun setTheme(themeType: ThemeType)
    suspend fun setKeepSelectedCard(value: Boolean)
    suspend fun setLastViewedCardId(id: Long)
    suspend fun clearPreferences()
}
