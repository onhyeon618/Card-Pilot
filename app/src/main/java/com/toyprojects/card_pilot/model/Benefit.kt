package com.toyprojects.card_pilot.model

data class Benefit(
    val id: Long = 0,
    val name: String,
    val explanation: String? = null,
    val capAmount: Long,
    val usedAmount: Long,
    val displayOrder: Int
)
