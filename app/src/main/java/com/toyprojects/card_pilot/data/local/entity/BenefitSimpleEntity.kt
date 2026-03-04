package com.toyprojects.card_pilot.data.local.entity

import androidx.room.ColumnInfo

data class BenefitSimpleEntity(
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "name") val name: String
)
