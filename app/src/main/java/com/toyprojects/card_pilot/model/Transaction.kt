package com.toyprojects.card_pilot.model

data class Transaction(
    val merchant: String,
    val date: String,     // "MM.dd"
    val time: String,     // "HH:mm"
    val amount: Double,
    val monthGroup: String // "2024년 2월"
)
