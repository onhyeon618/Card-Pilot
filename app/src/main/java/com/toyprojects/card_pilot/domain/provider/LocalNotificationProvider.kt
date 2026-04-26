package com.toyprojects.card_pilot.domain.provider

interface LocalNotificationProvider {
    fun sendNotification(content: String)
}
