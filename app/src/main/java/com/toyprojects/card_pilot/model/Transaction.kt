package com.toyprojects.card_pilot.model

data class Transaction(
    val id: Long,
    val merchant: String,
    val dateTime: java.time.LocalDateTime,
    val amount: Long
)
