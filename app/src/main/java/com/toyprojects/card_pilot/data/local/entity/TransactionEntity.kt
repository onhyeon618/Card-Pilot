package com.toyprojects.card_pilot.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = BenefitEntity::class,
            parentColumns = ["id"],
            childColumns = ["benefitId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(
            value = ["benefitId", "dateTime"],
            orders = [Index.Order.ASC, Index.Order.DESC]
        )
    ]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val benefitId: Long,
    val merchant: String,
    val dateTime: java.time.LocalDateTime,
    val amount: Long
)
