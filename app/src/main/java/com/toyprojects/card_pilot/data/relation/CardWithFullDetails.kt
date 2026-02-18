package com.toyprojects.card_pilot.data.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.toyprojects.card_pilot.data.entity.BenefitEntity
import com.toyprojects.card_pilot.data.entity.CardEntity

data class CardWithFullDetails(
    @Embedded val card: CardEntity,
    @Relation(
        entity = BenefitEntity::class,
        parentColumn = "id",
        entityColumn = "cardId"
    )
    val benefits: List<BenefitWithTransactions>
)
