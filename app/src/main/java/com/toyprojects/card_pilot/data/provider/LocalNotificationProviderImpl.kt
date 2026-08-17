package com.toyprojects.card_pilot.data.provider

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.toyprojects.card_pilot.MainActivity
import com.toyprojects.card_pilot.R
import com.toyprojects.card_pilot.domain.provider.LocalNotificationProvider

class LocalNotificationProviderImpl(private val context: Context) : LocalNotificationProvider {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val channelId = "card_pilot_local_notification"

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "카드 결제 알림"
            val descriptionText = "카드 결제 내역을 파싱하여 알려줍니다."
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun sendNotification(
        content: String,
        amount: String?,
        place: String?,
        date: String?,
        time: String?,
        cardName: String?
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (androidx.core.content.ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }

        val notificationId = System.currentTimeMillis().toInt()
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(LocalNotificationProvider.EXTRA_NAVIGATE_TO, LocalNotificationProvider.TARGET_EDIT_TRANSACTION)
            putExtra(LocalNotificationProvider.EXTRA_AMOUNT, amount)
            putExtra(LocalNotificationProvider.EXTRA_MERCHANT, place)
            putExtra(LocalNotificationProvider.EXTRA_DATE, date)
            putExtra(LocalNotificationProvider.EXTRA_TIME, time)
            putExtra(LocalNotificationProvider.EXTRA_CARD_NAME, cardName)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("")
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(notificationId, builder.build())
    }
}
