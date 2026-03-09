package com.toyprojects.card_pilot.domain.model

import java.time.LocalDateTime

data class NotificationMessage(
    val id: Long = 0,
    val packageName: String,
    val title: String,
    val content: String,
    val timestamp: LocalDateTime
)
