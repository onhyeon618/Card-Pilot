package com.toyprojects.card_pilot.domain.parser

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * 하나카드 푸시 알림 파서
 *
 * 예시:
 * 제목: (결제) 12,300원
 * 본문: 사용처 이름 / 신용(일시불,1*3*) / 04.23 12:30 / 누적이용금액 123,000원
 */
class HanaNotificationParser : NotificationParser {
    override val supportedPackage = "com.hanaskcard.paycla"

    companion object {
        private val AMOUNT_REGEX = Regex("""([0-9,]+)원""")
        private val TIMESTAMP_REGEX = Regex("""(\d{1,2}\.\d{1,2}\s+\d{1,2}:\d{2})""")
    }

    override fun canParse(title: String, content: String): Boolean {
        // 하나카드는 본문에 '/'로 구분된 데이터가 들어옴
        val parts = content.split("/").map { it.trim() }
        if (parts.size < 3) return false

        val hasAmountInTitle = title.contains("결제") || title.contains("승인")
        val isTimestampValid = TIMESTAMP_REGEX.containsMatchIn(parts[2])

        return hasAmountInTitle && isTimestampValid
    }

    override fun extractAmount(title: String?, content: String): String? {
        if (title != null) {
            val match = AMOUNT_REGEX.find(title)
            if (match != null) {
                return match.groupValues[1]
            }
        }
        return null
    }

    override fun extractPlace(title: String?, content: String): String {
        val parts = content.split("/").map { it.trim() }
        return parts.getOrNull(0) ?: ""
    }

    override fun extractCardName(content: String): String? {
        return null
    }

    override fun extractTimestamp(content: String, defaultTimestamp: LocalDateTime): LocalDateTime {
        val parts = content.split("/").map { it.trim() }
        val timeString = parts.getOrNull(2) ?: return defaultTimestamp

        val match = TIMESTAMP_REGEX.find(timeString) ?: return defaultTimestamp

        val year = defaultTimestamp.year
        val formatter = DateTimeFormatter.ofPattern("yyyy/M.d H:m")

        return try {
            LocalDateTime.parse("$year/${match.groupValues[1]}", formatter)
        } catch (_: DateTimeParseException) {
            defaultTimestamp
        }
    }
}
