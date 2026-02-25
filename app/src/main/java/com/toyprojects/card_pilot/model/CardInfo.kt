package com.toyprojects.card_pilot.model

data class CardInfo(
    val id: Long = 0,
    val name: String,
    val image: String,
    val usageAmount: Long,
    val benefits: List<Benefit>,
    val displayOrder: Int
)
