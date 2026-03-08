package com.toyprojects.card_pilot.ui.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toyprojects.card_pilot.domain.repository.SettingsRepository
import com.toyprojects.card_pilot.ui.feature.settings.model.CardCompanyApp
import com.toyprojects.card_pilot.ui.feature.settings.provider.DeviceAppProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NotificationSettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val deviceAppProvider: DeviceAppProvider
) : ViewModel() {

    val notiReceiveEnabled: StateFlow<Boolean> = settingsRepository.notiReceiveEnabled
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    val notiReceiveApps: StateFlow<Set<String>> = settingsRepository.notiReceiveApps
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptySet()
        )

    val installedCardApps: StateFlow<List<CardCompanyApp>> =
        settingsRepository.customAddedApps
            .combine(kotlinx.coroutines.flow.flow { emit(Unit) }) { customApps, _ ->
                deviceAppProvider.getInstalledCardApps(customApps)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    private val _allInstalledApps = MutableStateFlow<List<CardCompanyApp>>(emptyList())
    val allInstalledApps: StateFlow<List<CardCompanyApp>> = _allInstalledApps.asStateFlow()

    private val _isLoadingAllApps = MutableStateFlow(false)
    val isLoadingAllApps: StateFlow<Boolean> = _isLoadingAllApps.asStateFlow()

    fun toggleNotiReceive() {
        viewModelScope.launch {
            val currentValue = notiReceiveEnabled.value
            settingsRepository.setNotiReceiveEnabled(!currentValue)
        }
    }

    fun toggleAppNotiReceive(appName: String) {
        viewModelScope.launch {
            val currentApps = notiReceiveApps.value.toMutableSet()
            if (currentApps.contains(appName)) {
                currentApps.remove(appName)
            } else {
                currentApps.add(appName)
            }
            settingsRepository.setNotiReceiveApps(currentApps)
        }
    }

    fun addCustomApp(appName: String) {
        viewModelScope.launch {
            // 알림 수신 목록에 추가
            val currentReceive = notiReceiveApps.value.toMutableSet()
            currentReceive.add(appName)
            settingsRepository.setNotiReceiveApps(currentReceive)

            // 커스텀 목록에 추가
            val customAppsFlow = settingsRepository.customAddedApps.stateIn(viewModelScope)
            val currentCustom = customAppsFlow.value.toMutableSet()
            if (!currentCustom.contains(appName)) {
                currentCustom.add(appName)
                settingsRepository.setCustomAddedApps(currentCustom)
            }
        }
    }

    fun loadAllInstalledApps() {
        if (_allInstalledApps.value.isNotEmpty() || _isLoadingAllApps.value) return

        viewModelScope.launch {
            _isLoadingAllApps.value = true
            val excludePackages = installedCardApps.value.map { it.packageName }.toSet()
            _allInstalledApps.value = deviceAppProvider.getAllInstalledApps(excludePackages)
            _isLoadingAllApps.value = false
        }
    }
}
