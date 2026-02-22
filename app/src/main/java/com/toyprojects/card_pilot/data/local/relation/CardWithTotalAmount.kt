package com.toyprojects.card_pilot.data.local.relation

import androidx.room.Embedded
import com.toyprojects.card_pilot.data.local.entity.CardInfoEntity

data class CardWithTotalAmount(
    @Embedded val card: CardInfoEntity,
    val totalAmount: Long
)
