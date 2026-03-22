package com.toyprojects.card_pilot.domain.parser

class NotificationParserFactory {
    private val parsers: List<NotificationParser> = listOf(
//        KBNotificationParser(),
//        HyundaiNotificationParser(),
//        SamsungNotificationParser(),
//        ShinhanNotificationParser(),
//        LotteNotificationParser(),
//        HanaNotificationParser(),
//        WooriNotificationParser(),
//        NHNotificationParser(),
//        NaverNotificationParser()
    )
    private val defaultParser = DefaultNotificationParser()

    fun getParser(packageName: String): NotificationParser {
        return parsers.find { packageName in it.supportedPackages } ?: defaultParser
    }
}
