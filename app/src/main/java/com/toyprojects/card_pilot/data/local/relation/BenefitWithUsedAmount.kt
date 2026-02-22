package com.toyprojects.card_pilot.data.local.relation

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.toyprojects.card_pilot.data.local.entity.BenefitEntity

data class BenefitWithUsedAmount(
    @Embedded val benefit: BenefitEntity,
    @ColumnInfo(name = "usedAmount") val usedAmount: Long
)
