package com.toyprojects.card_pilot.ui.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toyprojects.card_pilot.domain.repository.ThemePreferenceRepository
import com.toyprojects.card_pilot.model.ThemeType
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val themePreferenceRepository: ThemePreferenceRepository
) : ViewModel() {

    val currentTheme: StateFlow<ThemeType> = themePreferenceRepository.themeType
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ThemeType.PURPLE
        )

    fun updateTheme(themeType: ThemeType) {
        viewModelScope.launch {
            themePreferenceRepository.setTheme(themeType)
        }
    }
}
