package com.toyprojects.card_pilot.domain.parser

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * 롯데카드 푸시 알림 파서
 *
 * 예시:
 * 푸시 알림 제목: 사용처 이름
 * 푸시 알림 내용:
 * 10,000원 승인
 * LOCA LIKIT 1.2(1*2*)
 * 일시불, 03/13 15:30
 * 누적금액 123,000원
 */
class LotteNotificationParser : NotificationParser {
    override val supportedPackage = "com.lcacApp"

    companion object {
        private val AMOUNT_REGEX = Regex("([0-9,]+)원")
        private val TIMESTAMP_REGEX = Regex("""(\d{1,2}/\d{1,2} \d{1,2}:\d{2})""")
        private val CARD_NAME_REGEX = Regex("""^[^(]+""")
    }

    override fun canParse(title: String, content: String): Boolean {
        val lines = content.split("\n", "\r").map { it.trim() }.filter { it.isNotBlank() }
        if (lines.size != 4) return false

        val isFirstLineValid = AMOUNT_REGEX.containsMatchIn(lines[0])
        val isThirdLineValid = TIMESTAMP_REGEX.containsMatchIn(lines[2])

        return isFirstLineValid && isThirdLineValid
    }

    override fun extractAmount(title: String?, content: String): String {
        return AMOUNT_REGEX.find(content)!!.value
    }

    override fun extractPlace(title: String?, content: String): String? {
        return title
    }

    override fun extractCardName(content: String): String {
        val lines = content.split("\n", "\r").map { it.trim() }.filter { it.isNotBlank() }
        val rawCardName = lines[1]
        return CARD_NAME_REGEX.find(rawCardName)?.value?.trim() ?: rawCardName
    }

    override fun extractTimestamp(content: String, defaultTimestamp: LocalDateTime): LocalDateTime {
        val timeString = TIMESTAMP_REGEX.find(content)!!.groupValues[1]
        val year = defaultTimestamp.year
        val formatter = DateTimeFormatter.ofPattern("yyyy/M/d H:m")
        return try {
            LocalDateTime.parse("$year/$timeString", formatter)
        } catch (_: DateTimeParseException) {
            defaultTimestamp
        }
    }
}
