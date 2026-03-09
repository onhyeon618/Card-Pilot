package com.toyprojects.card_pilot.domain.repository

import com.toyprojects.card_pilot.domain.model.NotificationMessage
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun getAllNotifications(): Flow<List<NotificationMessage>>
    suspend fun insertNotification(notification: NotificationMessage)
    suspend fun deleteNotificationById(id: Long)
    suspend fun deleteAllNotifications()
}
