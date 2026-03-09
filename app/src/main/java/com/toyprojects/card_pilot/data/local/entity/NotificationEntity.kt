package com.toyprojects.card_pilot.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.toyprojects.card_pilot.domain.model.NotificationMessage
import java.time.LocalDateTime

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val packageName: String,
    val title: String,
    val content: String,
    val timestamp: LocalDateTime
) {
    fun toDomainModel(): NotificationMessage {
        return NotificationMessage(
            id = id,
            packageName = packageName,
            title = title,
            content = content,
            timestamp = timestamp
        )
    }
}

fun NotificationMessage.toEntity(): NotificationEntity {
    return NotificationEntity(
        id = id,
        packageName = packageName,
        title = title,
        content = content,
        timestamp = timestamp
    )
}
