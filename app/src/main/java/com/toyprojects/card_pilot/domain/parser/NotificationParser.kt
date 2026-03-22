package com.toyprojects.card_pilot.domain.parser

interface NotificationParser {
    val supportedPackages: List<String>
    fun extractAmount(content: String): String?
    fun extractPlace(content: String): String?
}
