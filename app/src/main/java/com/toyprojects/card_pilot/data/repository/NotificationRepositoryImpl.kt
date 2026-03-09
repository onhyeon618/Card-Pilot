package com.toyprojects.card_pilot.data.repository

import com.toyprojects.card_pilot.data.local.dao.NotificationDao
import com.toyprojects.card_pilot.data.local.entity.toEntity
import com.toyprojects.card_pilot.domain.model.NotificationMessage
import com.toyprojects.card_pilot.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotificationRepositoryImpl(
    private val notificationDao: NotificationDao
) : NotificationRepository {

    override fun getAllNotifications(): Flow<List<NotificationMessage>> {
        return notificationDao.getAllNotifications().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun insertNotification(notification: NotificationMessage) {
        notificationDao.insertNotification(notification.toEntity())
    }

    override suspend fun deleteNotificationById(id: Long) {
        notificationDao.deleteNotificationById(id)
    }

    override suspend fun deleteAllNotifications() {
        notificationDao.deleteAllNotifications()
    }
}
