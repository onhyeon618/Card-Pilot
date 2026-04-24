package com.toyprojects.card_pilot.domain.parser

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * KB국민카드 푸시 알림 파서
 *
 * 예시:
 * KB국민카드1234승인
 * 홍*동님
 * 1,230원 일시불
 * 04/01 12:30
 * 사용처 이름
 * 누적123,000원
 */
class KBNotificationParser : NotificationParser {
    override val supportedPackage = "com.kbcard.cxh.appcard"

    companion object {
        private val TIMESTAMP_REGEX = Regex("""(\d{1,2}/\d{1,2}\s+\d{1,2}:\d{2})""")
        private val AMOUNT_REGEX = Regex("""^([0-9,]+)원""")
    }

    override fun canParse(title: String, content: String): Boolean {
        val lines = content.split("\n", "\r").map { it.trim() }.filter { it.isNotBlank() }
        if (lines.size < 5) return false

        val isFirstLineValid = lines[0].contains("국민카드") && lines[0].contains("승인")
        val isThirdLineValid = AMOUNT_REGEX.containsMatchIn(lines[2])
        val isFourthLineValid = TIMESTAMP_REGEX.containsMatchIn(lines[3])

        return isFirstLineValid && isThirdLineValid && isFourthLineValid
    }

    override fun extractAmount(title: String?, content: String): String {
        val lines = content.split("\n", "\r").map { it.trim() }.filter { it.isNotBlank() }
        return AMOUNT_REGEX.find(lines[2])!!.groupValues[1]
    }

    override fun extractPlace(title: String?, content: String): String {
        val lines = content.split("\n", "\r").map { it.trim() }.filter { it.isNotBlank() }
        return lines[4]
    }

    override fun extractCardName(content: String): String? {
        return null
    }

    override fun extractTimestamp(content: String, defaultTimestamp: LocalDateTime): LocalDateTime {
        val lines = content.split("\n", "\r").map { it.trim() }.filter { it.isNotBlank() }
        val timeString = TIMESTAMP_REGEX.find(lines[3])!!.groupValues[1]

        val year = defaultTimestamp.year
        val formatter = DateTimeFormatter.ofPattern("yyyy/M/d H:m")
        return try {
            LocalDateTime.parse("$year/$timeString", formatter)
        } catch (_: DateTimeParseException) {
            defaultTimestamp
        }
    }
}
