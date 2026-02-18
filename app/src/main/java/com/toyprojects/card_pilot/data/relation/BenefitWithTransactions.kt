package com.toyprojects.card_pilot.data.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.toyprojects.card_pilot.data.entity.BenefitEntity
import com.toyprojects.card_pilot.data.entity.TransactionEntity

data class BenefitWithTransactions(
    @Embedded val benefit: BenefitEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "benefitId"
    )
    val transactions: List<TransactionEntity>
)
