package com.toyprojects.card_pilot.model

data class Benefit(
    val category: String,
    val used: Double,
    val total: Double,
    val explanation: String? = null
)
