package com.toyprojects.card_pilot.model

data class Transaction(
    val merchant: String,
    val date: String,     // "MM.dd"
    val time: String,     // "HH:mm"
    val amount: Double,
    val eligible: Double? = null,  // Benefit 모델로 옮겨야 함
    val monthGroup: String // "2024년 2월"
)
