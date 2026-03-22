package com.toyprojects.card_pilot.domain.parser

class DefaultNotificationParser : NotificationParser {
    override val supportedPackages = emptyList<String>()

    companion object {
        private val AMOUNT_REGEX = Regex("([0-9,]+)원")
        private val TIMESTAMP_REGEX = Regex("""(\d{1,2}/\d{1,2}\s+\d{1,2}:\d{2})""")
        private val EXCLUDED_KEYWORDS_IN_PLACE = listOf(
            "원", "승인", "결제", "일시불", "할부", "취소", ":", "/"
        )
    }

    override fun extractAmount(content: String): String? {
        val match = AMOUNT_REGEX.find(content)
        return match?.value
    }

    override fun extractPlace(title: String?, content: String): String? {
        val lines = content.split("\n", "\r", " ").filter { it.isNotBlank() }
        val filtered = lines.filter { line ->
            EXCLUDED_KEYWORDS_IN_PLACE.none { keyword -> line.contains(keyword) }
        }
        return filtered.lastOrNull()
    }

    override fun extractCardName(content: String): String? = null

    override fun extractTimestamp(content: String, defaultTimestamp: java.time.LocalDateTime): java.time.LocalDateTime {
        val match = TIMESTAMP_REGEX.find(content)
        if (match != null) {
            val timeString = match.groupValues[1]
            val year = defaultTimestamp.year
            val formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy/M/d H:m")
            return try {
                java.time.LocalDateTime.parse("$year/$timeString", formatter)
            } catch (e: java.time.format.DateTimeParseException) {
                defaultTimestamp
            }
        }
        return defaultTimestamp
    }

}
