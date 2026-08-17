package com.toyprojects.card_pilot.domain.provider

interface LocalNotificationProvider {
    fun sendNotification(
        content: String,
        amount: String? = null,
        place: String? = null,
        date: String? = null,
        time: String? = null,
        cardName: String? = null
    )

    companion object {
        const val EXTRA_NAVIGATE_TO = "navigate_to"
        const val TARGET_EDIT_TRANSACTION = "edit_transaction"
        const val EXTRA_AMOUNT = "amount"
        const val EXTRA_MERCHANT = "merchant"
        const val EXTRA_DATE = "date"
        const val EXTRA_TIME = "time"
        const val EXTRA_CARD_NAME = "card_name"
    }
}
