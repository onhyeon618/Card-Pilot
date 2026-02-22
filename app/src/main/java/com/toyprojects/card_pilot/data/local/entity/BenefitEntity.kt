package com.toyprojects.card_pilot.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "benefits",
    foreignKeys = [
        ForeignKey(
            entity = CardInfoEntity::class,
            parentColumns = ["id"],
            childColumns = ["cardId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["cardId", "name"], unique = true)
    ]
)
data class BenefitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cardId: Long,
    val name: String,
    val capAmount: Long,
    val explanation: String?,
    val displayOrder: Int
)
