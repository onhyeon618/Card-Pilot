package com.toyprojects.card_pilot.model

data class BenefitProperty(
    val id: Long = 0,
    val name: String,
    val explanation: String? = null,
    val capAmount: Long,
    val dailyLimit: Long,
    val oneTimeLimit: Long,
    val rate: Float
)