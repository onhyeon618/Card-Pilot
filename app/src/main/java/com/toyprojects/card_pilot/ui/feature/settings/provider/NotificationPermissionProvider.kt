package com.toyprojects.card_pilot.ui.feature.settings.provider

interface NotificationPermissionProvider {
    fun hasNotificationAccess(): Boolean
    fun hasPostNotificationsPermission(): Boolean
}
