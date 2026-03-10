package com.toyprojects.card_pilot.domain.parser

/// 지출 알림 메시지 내용에서 결제 금액과 사용처를 추출하는 파서
class NotificationParser {
    companion object {
        private val AMOUNT_REGEX = Regex("([0-9,]+)원")
        private val EXCLUDED_KEYWORDS_IN_PLACE = listOf(
            "원", "승인", "결제", "일시불", "할부", "취소", ":", "/"
        )
    }

    fun extractAmount(content: String): String? {
        val match = AMOUNT_REGEX.find(content)
        return match?.value
    }

    // TODO: 추후 로직 조정 필요
    fun extractPlace(content: String): String? {
        val lines = content.split("\n", "\r", " ").filter { it.isNotBlank() }
        val filtered = lines.filter { line ->
            EXCLUDED_KEYWORDS_IN_PLACE.none { keyword -> line.contains(keyword) }
        }
        return filtered.lastOrNull()
    }
}
