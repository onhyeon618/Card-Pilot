package com.toyprojects.card_pilot.domain.parser

class NotificationParserFactory {
    private val parsers: List<NotificationParser> = listOf(
//        KBNotificationParser(),
        HyundaiNotificationParser(),
//        SamsungNotificationParser(),
        ShinhanNotificationParser(),
        LotteNotificationParser(),
//        HanaNotificationParser(),
        WooriNotificationParser(),
//        NHNotificationParser(),
//        NaverNotificationParser()
    )
    private val defaultParser = DefaultNotificationParser()

    fun getParser(packageName: String, title: String, content: String): NotificationParser {
        val parser = parsers.find { it.supportedPackage == packageName }
        return if (parser != null && parser.canParse(title, content)) {
            parser
        } else {
            defaultParser
        }
    }
}
