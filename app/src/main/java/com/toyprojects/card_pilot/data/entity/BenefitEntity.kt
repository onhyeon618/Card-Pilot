package com.toyprojects.card_pilot.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "benefits",
    foreignKeys = [
        ForeignKey(
            entity = CardEntity::class,
            parentColumns = ["id"],
            childColumns = ["cardId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["cardId"])]
)
data class BenefitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val cardId: Int, // Foreign Key
    val name: String,
    val usedAmount: Long,
    val totalAmount: Long,
    val eligiblePerUse: Long? = null,
    val explanation: String? = null
)
