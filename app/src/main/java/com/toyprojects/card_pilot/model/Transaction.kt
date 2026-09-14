package com.toyprojects.card_pilot.model

import java.time.LocalDateTime

data class Transaction(
    val id: Long = 0,
    val merchant: String,
    val dateTime: LocalDateTime,
    val amount: Long,
    val appliedAmount: Long
)
