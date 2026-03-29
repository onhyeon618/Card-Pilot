package com.toyprojects.card_pilot.domain.parser

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * 현대카드 푸시 알림 파서
 *
 * 예시:
 * 홍길동 님, 카드명 승인
 * 123,000원 일시불, 3/26 12:30
 * 사용처 이름
 * 누적1,230,000원
 */
class HyundaiNotificationParser : NotificationParser {
    override val supportedPackage = "com.hyundaicard.appcard"

    companion object {
        private val CARD_NAME_REGEX = Regex("""님,\s*(.+)\s*승인""")
        private val AMOUNT_REGEX = Regex("""^([0-9,]+)원""")
        private val TIMESTAMP_REGEX = Regex("""(\d{1,2}/\d{1,2}\s+\d{1,2}:\d{2})""")
    }

    override fun canParse(title: String, content: String): Boolean {
        val lines = content.split("\n", "\r").map { it.trim() }.filter { it.isNotBlank() }
        if (lines.size < 3) return false

        val isFirstLineValid = CARD_NAME_REGEX.containsMatchIn(lines[0])
        val isSecondLineValid = AMOUNT_REGEX.containsMatchIn(lines[1]) && TIMESTAMP_REGEX.containsMatchIn(lines[1])

        return isFirstLineValid && isSecondLineValid
    }

    override fun extractAmount(content: String): String {
        val lines = content.split("\n", "\r").map { it.trim() }.filter { it.isNotBlank() }
        return AMOUNT_REGEX.find(lines[1])!!.groupValues[1]
    }

    override fun extractPlace(title: String?, content: String): String {
        val lines = content.split("\n", "\r").map { it.trim() }.filter { it.isNotBlank() }
        return lines[2]
    }

    override fun extractCardName(content: String): String {
        val lines = content.split("\n", "\r").map { it.trim() }.filter { it.isNotBlank() }
        return CARD_NAME_REGEX.find(lines[0])!!.groupValues[1].trim()
    }

    override fun extractTimestamp(content: String, defaultTimestamp: LocalDateTime): LocalDateTime {
        val lines = content.split("\n", "\r").map { it.trim() }.filter { it.isNotBlank() }
        val timeString = TIMESTAMP_REGEX.find(lines[1])!!.groupValues[1]

        val year = defaultTimestamp.year
        val formatter = DateTimeFormatter.ofPattern("yyyy/M/d H:m")
        return try {
            LocalDateTime.parse("$year/$timeString", formatter)
        } catch (_: DateTimeParseException) {
            defaultTimestamp
        }
    }
}
