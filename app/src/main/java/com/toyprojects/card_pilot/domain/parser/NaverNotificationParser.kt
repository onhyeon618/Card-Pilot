package com.toyprojects.card_pilot.domain.parser

import java.time.LocalDateTime

/**
 * 네이버페이 푸시 알림 파서
 *
 * 예시:
 * {사용처이름}에서 12,300원 결제되었습니다. (결제 수단: 카드 간편결제)
 */
class NaverNotificationParser : NotificationParser {
    override val supportedPackage = "com.naver.pay.app"

    companion object {
        private val PAYMENT_INFO_REGEX = Regex("""(.*?)에서\s+([0-9,]+)원\s+결제되었습니다""")
    }

    override fun canParse(title: String, content: String): Boolean {
        return PAYMENT_INFO_REGEX.containsMatchIn(content)
    }

    override fun extractAmount(title: String?, content: String): String {
        return PAYMENT_INFO_REGEX.find(content)!!.groupValues[2]
    }

    override fun extractPlace(title: String?, content: String): String {
        return PAYMENT_INFO_REGEX.find(content)!!.groupValues[1].trim()
    }

    override fun extractCardName(content: String): String? {
        return null
    }

    override fun extractTimestamp(content: String, defaultTimestamp: LocalDateTime): LocalDateTime {
        return defaultTimestamp
    }
}
