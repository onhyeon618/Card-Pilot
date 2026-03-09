package com.toyprojects.card_pilot.ui.feature.settings.provider

import android.content.Context
import androidx.core.app.NotificationManagerCompat

class NotificationPermissionProviderImpl(
    private val context: Context
) : NotificationPermissionProvider {

    override fun hasNotificationAccess(): Boolean {
        return NotificationManagerCompat
            .getEnabledListenerPackages(context)
            .contains(context.packageName)
    }
}
