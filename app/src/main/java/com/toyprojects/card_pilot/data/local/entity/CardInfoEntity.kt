package com.toyprojects.card_pilot.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cards")
data class CardInfoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val image: String,
    val displayOrder: Int
)

data class CardOrderUpdate(
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "displayOrder") val displayOrder: Int
)
