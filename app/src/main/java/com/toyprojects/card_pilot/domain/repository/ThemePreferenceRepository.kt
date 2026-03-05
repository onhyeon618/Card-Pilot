package com.toyprojects.card_pilot.domain.repository

import com.toyprojects.card_pilot.model.ThemeType
import kotlinx.coroutines.flow.Flow

interface ThemePreferenceRepository {
    val themeType: Flow<ThemeType>
    suspend fun setTheme(themeType: ThemeType)
}
