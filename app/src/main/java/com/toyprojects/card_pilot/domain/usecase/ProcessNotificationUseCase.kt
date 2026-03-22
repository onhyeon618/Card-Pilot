package com.toyprojects.card_pilot.domain.usecase

import com.toyprojects.card_pilot.domain.model.NotificationMessage
import com.toyprojects.card_pilot.domain.parser.NotificationParserFactory
import com.toyprojects.card_pilot.domain.repository.NotificationRepository
import com.toyprojects.card_pilot.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.first
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class ProcessNotificationUseCase(
    private val notificationRepository: NotificationRepository,
    private val notificationParserFactory: NotificationParserFactory,
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(
        packageName: String,
        title: String,
        content: String,
        postTimeMillis: Long
    ) {
        val isEnabled = settingsRepository.notiReceiveEnabled.first()
        if (!isEnabled) return

        val allowedApps = settingsRepository.notiReceiveApps.first()
        if (!allowedApps.contains(packageName)) return

        if (!isPaymentRelated(content)) return

        val parser = notificationParserFactory.getParser(packageName)
        val amount = parser.extractAmount(content) ?: ""
        val place = parser.extractPlace(title, content) ?: ""
        val cardName = parser.extractCardName(content)

        val defaultTimestamp = mapToLocalDateTime(postTimeMillis)
        val timestamp = parser.extractTimestamp(content, defaultTimestamp)

        val message = NotificationMessage(
            packageName = packageName,
            title = title,
            content = content,
            amount = amount,
            place = place,
            cardName = cardName,
            timestamp = timestamp
        )
        notificationRepository.insertNotification(message)
    }

    private fun isPaymentRelated(content: String): Boolean {
        val hasCurrency = content.contains(CURRENCY_KRW)
        val hasPaymentKeyword = PAYMENT_KEYWORDS.any { content.contains(it) }
        return hasCurrency && hasPaymentKeyword
    }

    private fun mapToLocalDateTime(timeMillis: Long): LocalDateTime {
        return Instant.ofEpochMilli(timeMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
    }

    companion object {
        private const val CURRENCY_KRW = "원"
        private val PAYMENT_KEYWORDS = listOf("승인", "결제", "취소", "할부")
    }
}
