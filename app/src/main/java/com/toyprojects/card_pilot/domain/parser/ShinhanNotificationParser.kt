package com.toyprojects.card_pilot.domain.parser

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * 신한카드 푸시 알림 파서
 *
 * 예시:
 * [신한카드(1234)승인] 홍*동
 * - 승인금액: 10,000원(일시불)
 * - 승인일시: 03/26 12:30
 * - 가맹점명: 사용처 이름
 * - 누적금액: 123,000원
 * [신한카드 1544-7000]
 */
class ShinhanNotificationParser : NotificationParser {
    override val supportedPackage = "com.shcard.smartpay"

    companion object {
        private val AMOUNT_REGEX = Regex("""승인금액:\s*([0-9,]+)원""")
        private val DATE_REGEX = Regex("""승인일시:\s*(\d{1,2}/\d{1,2}\s+\d{1,2}:\d{2})""")
        private val PLACE_REGEX = Regex("""가맹점명:\s*(.+)""")
    }

    override fun canParse(title: String, content: String): Boolean {
        return content.contains("신한카드") &&
                AMOUNT_REGEX.containsMatchIn(content) &&
                DATE_REGEX.containsMatchIn(content) &&
                PLACE_REGEX.containsMatchIn(content)
    }

    override fun extractAmount(title: String?, content: String): String {
        return AMOUNT_REGEX.find(content)!!.groupValues[1]
    }

    override fun extractPlace(title: String?, content: String): String {
        return PLACE_REGEX.find(content)!!.groupValues[1].trim()
    }

    override fun extractCardName(content: String): String? {
        return null
    }

    override fun extractTimestamp(content: String, defaultTimestamp: LocalDateTime): LocalDateTime {
        val timeString = DATE_REGEX.find(content)!!.groupValues[1]
        val year = defaultTimestamp.year
        val formatter = DateTimeFormatter.ofPattern("yyyy/M/d H:m")

        return try {
            LocalDateTime.parse("$year/$timeString", formatter)
        } catch (_: DateTimeParseException) {
            defaultTimestamp
        }
    }
}
