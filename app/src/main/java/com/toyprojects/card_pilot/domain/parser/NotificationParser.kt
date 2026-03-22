package com.toyprojects.card_pilot.domain.parser

import java.time.LocalDateTime

interface NotificationParser {
    val supportedPackages: List<String>
    fun extractAmount(content: String): String?
    fun extractPlace(title: String?, content: String): String?
    fun extractCardName(content: String): String?
    fun extractTimestamp(content: String, defaultTimestamp: LocalDateTime): LocalDateTime
}
