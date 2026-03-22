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
    val amount: String = "",
    val place: String = "",
    val timestamp: LocalDateTime
)

fun NotificationMessage.toEntity(): NotificationEntity {
    return NotificationEntity(
        id = id,
        packageName = packageName,
        title = title,
        content = content,
        amount = amount,
        place = place,
        timestamp = timestamp
    )
}
