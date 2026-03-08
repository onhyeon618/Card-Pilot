package com.toyprojects.card_pilot.ui.feature.settings.provider

import com.toyprojects.card_pilot.ui.feature.settings.model.CardCompanyApp

interface DeviceAppProvider {
    suspend fun getInstalledCardApps(customApps: Set<String>): List<CardCompanyApp>
    suspend fun getAllInstalledApps(excludePackages: Set<String>): List<CardCompanyApp>
}
