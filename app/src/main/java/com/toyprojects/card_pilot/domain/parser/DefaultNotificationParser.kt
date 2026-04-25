package com.toyprojects.card_pilot.domain.parser

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class DefaultNotificationParser : NotificationParser {
    override val supportedPackage = ""

    override fun canParse(title: String, content: String): Boolean {
        if (title.startsWith("(광고)") || content.startsWith("(광고)")) return false
        if (title.contains("취소") || title.contains("거절")) return false
        if (content.contains("취소") || content.contains("거절")) return false
        return true
    }

    companion object {
        private val AMOUNT_REGEX = Regex("([0-9,]+)원")
        private val TIMESTAMP_REGEX = Regex("""(\d{1,2}[/.-]\d{1,2}\s+\d{1,2}:\d{2})""")

        // 사용처 추출 명시적 패턴
        private val PLACE_PREFIX_REGEX = Regex("""(?:가맹점명?|사용처|결제처)\s*:?\s+(.+)""")
        private val PLACE_SUFFIX_REGEX = Regex("""(.*?)에서\s+.*결제""")

        // 추출 후 정제할 노이즈
        private val PLACE_CLEANUP_REGEX = Regex("""(가맹점명?|사용처|결제처)\s*:?|\s*에서\s*.*결제.*|\s*에서\s*$""")
    }

    override fun extractAmount(title: String?, content: String): String? {
        val lines = if (content.contains("\n")) {
            content.split("\n", "\r").map { it.trim() }.filter { it.isNotBlank() }
        } else if (content.contains("/")) {
            content.split("/").map { it.trim() }.filter { it.isNotBlank() }
        } else {
            listOf(content.trim())
        }

        // 누적/잔액 뒤에 오는 금액은 무시
        for (line in lines) {
            val matches = AMOUNT_REGEX.findAll(line).toList()
            for (match in matches) {
                val prefix = line.substring(0, match.range.first)
                if (!prefix.contains("누적") && !prefix.contains("잔액")) {
                    return match.groupValues[1]
                }
            }
        }

        return title?.let { AMOUNT_REGEX.find(it)?.groupValues?.get(1) }
    }

    override fun extractPlace(title: String?, content: String): String? {
        val lines = if (content.contains("\n")) {
            content.split("\n", "\r").map { it.trim() }.filter { it.isNotBlank() }
        } else if (content.contains("/")) {
            content.split("/").map { it.trim() }.filter { it.isNotBlank() }
        } else {
            listOf(content.trim())
        }

        for (line in lines) {
            PLACE_PREFIX_REGEX.find(line)?.let { return cleanUpPlace(it.groupValues[1]) }
            PLACE_SUFFIX_REGEX.find(line)?.let { return cleanUpPlace(it.groupValues[1]) }
        }

        for (line in lines) {
            val isNoise = AMOUNT_REGEX.containsMatchIn(line) ||
                    TIMESTAMP_REGEX.containsMatchIn(line) ||
                    line.contains("승인") || line.contains("체크") ||
                    line.contains("신용") || line.contains("누적") ||
                    line.contains("잔액") || line.contains("카드")

            if (!isNoise) {
                val cleaned = cleanUpPlace(line)
                if (cleaned.isNotBlank()) return cleaned
            }
        }

        return null
    }

    private fun cleanUpPlace(rawPlace: String): String {
        return rawPlace.replace(PLACE_CLEANUP_REGEX, "").trim()
    }

    override fun extractCardName(content: String): String? = null

    override fun extractTimestamp(content: String, defaultTimestamp: LocalDateTime): LocalDateTime {
        val match = TIMESTAMP_REGEX.find(content)
        if (match != null) {
            // . 이나 - 를 / 로 통일하여 파싱
            val timeString = match.groupValues[1].replace(".", "/").replace("-", "/")
            val year = defaultTimestamp.year
            val formatter = DateTimeFormatter.ofPattern("yyyy/M/d H:m")
            return try {
                LocalDateTime.parse("$year/$timeString", formatter)
            } catch (_: DateTimeParseException) {
                defaultTimestamp
            }
        }
        return defaultTimestamp
    }
}
