package com.toyprojects.card_pilot.data.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.toyprojects.card_pilot.data.entity.BenefitEntity
import com.toyprojects.card_pilot.data.entity.CardEntity

data class CardWithBenefits(
    @Embedded val card: CardEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "cardId"
    )
    val benefits: List<BenefitEntity>
)
