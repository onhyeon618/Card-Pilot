package com.toyprojects.card_pilot.domain.parser

class DefaultNotificationParser : NotificationParser {
    override val supportedPackages = emptyList<String>()

    companion object {
        private val AMOUNT_REGEX = Regex("([0-9,]+)원")
        private val EXCLUDED_KEYWORDS_IN_PLACE = listOf(
            "원", "승인", "결제", "일시불", "할부", "취소", ":", "/"
        )
    }

    override fun extractAmount(content: String): String? {
        val match = AMOUNT_REGEX.find(content)
        return match?.value
    }

    override fun extractPlace(content: String): String? {
        val lines = content.split("\n", "\r", " ").filter { it.isNotBlank() }
        val filtered = lines.filter { line ->
            EXCLUDED_KEYWORDS_IN_PLACE.none { keyword -> line.contains(keyword) }
        }
        return filtered.lastOrNull()
    }
}
