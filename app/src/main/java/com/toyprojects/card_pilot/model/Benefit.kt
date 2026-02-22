package com.toyprojects.card_pilot.model

data class Benefit(
    val name: String,
    val used: Long,
    val total: Long,
    val explanation: String? = null
)
