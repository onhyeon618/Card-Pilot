package com.toyprojects.card_pilot.data.local.relation

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.toyprojects.card_pilot.data.local.entity.BenefitEntity

data class BenefitWithUsedAmount(
    @Embedded val benefit: BenefitEntity,

    /// 적립/할인 사용량
    @ColumnInfo(name = "usedBenefitAmount") val usedBenefitAmount: Long,

    /// 실제 결제 금액
    @ColumnInfo(name = "usedPaymentAmount") val usedPaymentAmount: Long
)
