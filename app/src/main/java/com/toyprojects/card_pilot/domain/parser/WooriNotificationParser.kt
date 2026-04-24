package com.toyprojects.card_pilot.domain.parser

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * 우리카드 푸시 알림 파서
 *
 * 예시:
 * [일시불.승인(1234)]03/20 12:30
 * 123,000원 / 누적: 1,230,000원
 * 사용처 이름
 */
class WooriNotificationParser : NotificationParser {
    override val supportedPackage = "com.wooricard.smartapp"

    companion object {
        private val TIMESTAMP_REGEX = Regex("""(\d{1,2}/\d{1,2}\s+\d{1,2}:\d{2})""")
        private val AMOUNT_REGEX = Regex("""^([0-9,]+)원""")
    }

    override fun canParse(title: String, content: String): Boolean {
        val lines = content.split("\n", "\r").map { it.trim() }.filter { it.isNotBlank() }
        if (lines.size < 3) return false

        val isFirstLineValid = lines[0].contains("승인") && TIMESTAMP_REGEX.containsMatchIn(lines[0])
        val isSecondLineValid = AMOUNT_REGEX.containsMatchIn(lines[1])

        return isFirstLineValid && isSecondLineValid
    }

    override fun extractAmount(title: String?, content: String): String {
        val lines = content.split("\n", "\r").map { it.trim() }.filter { it.isNotBlank() }
        return AMOUNT_REGEX.find(lines[1])!!.groupValues[1]
    }

    override fun extractPlace(title: String?, content: String): String {
        val lines = content.split("\n", "\r").map { it.trim() }.filter { it.isNotBlank() }
        return lines[2]
    }

    override fun extractCardName(content: String): String? {
        return null
    }

    override fun extractTimestamp(content: String, defaultTimestamp: LocalDateTime): LocalDateTime {
        val lines = content.split("\n", "\r").map { it.trim() }.filter { it.isNotBlank() }
        val timeString = TIMESTAMP_REGEX.find(lines[0])!!.groupValues[1]

        val year = defaultTimestamp.year
        val formatter = DateTimeFormatter.ofPattern("yyyy/M/d H:m")
        return try {
            LocalDateTime.parse("$year/$timeString", formatter)
        } catch (_: DateTimeParseException) {
            defaultTimestamp
        }
    }
}
