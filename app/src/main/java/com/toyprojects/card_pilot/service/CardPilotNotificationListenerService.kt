package com.toyprojects.card_pilot.service

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.toyprojects.card_pilot.CardPilotApplication
import com.toyprojects.card_pilot.domain.usecase.ProcessNotificationUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class CardPilotNotificationListenerService : NotificationListenerService() {

    private lateinit var processNotificationUseCase: ProcessNotificationUseCase
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        val appContainer = (applicationContext as CardPilotApplication).container
        processNotificationUseCase = appContainer.processNotificationUseCase
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        sbn ?: return

        val packageName = sbn.packageName
        val notification = sbn.notification
        val title = notification.extras.getString(Notification.EXTRA_TITLE) ?: ""
        val content = notification.extras.getCharSequence(Notification.EXTRA_TEXT)?.toString() ?: ""
        val postTimeMillis = sbn.postTime

        if (title.isEmpty() && content.isEmpty()) return

        serviceScope.launch {
            processNotificationUseCase(
                packageName = packageName,
                title = title,
                content = content,
                postTimeMillis = postTimeMillis
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}
