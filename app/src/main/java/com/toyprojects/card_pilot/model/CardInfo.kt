package com.toyprojects.card_pilot.model

data class CardInfo(
    val id: Long = 0,
    val name: String,
    val image: String,
    val usageAmount: Long = 0,
    val benefits: List<Benefit> = emptyList(),
    val displayOrder: Int
)
