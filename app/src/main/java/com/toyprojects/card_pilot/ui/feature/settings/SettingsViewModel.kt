package com.toyprojects.card_pilot.ui.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toyprojects.card_pilot.domain.repository.SettingsRepository
import com.toyprojects.card_pilot.model.ThemeType
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val currentTheme: StateFlow<ThemeType> = settingsRepository.themeType
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ThemeType.PURPLE
        )

    fun updateTheme(themeType: ThemeType) {
        viewModelScope.launch {
            settingsRepository.setTheme(themeType)
        }
    }

    val keepSelectedCard: StateFlow<Boolean> = settingsRepository.keepSelectedCard
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    fun setKeepSelectedCard(value: Boolean) {
        viewModelScope.launch {
            settingsRepository.setKeepSelectedCard(value)
        }
    }
}
