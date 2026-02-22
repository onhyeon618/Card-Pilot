package com.toyprojects.card_pilot.domain.repository

import com.toyprojects.card_pilot.data.local.entity.BenefitEntity
import com.toyprojects.card_pilot.data.local.relation.BenefitWithUsedAmount
import kotlinx.coroutines.flow.Flow

interface BenefitRepository {
    fun getBenefitListOfCard(cardId: Long): Flow<List<BenefitWithUsedAmount>>
    suspend fun insertBenefit(benefit: BenefitEntity): Long
    suspend fun updateBenefits(benefits: List<BenefitEntity>)
    suspend fun deleteBenefits(benefits: List<BenefitEntity>)
    suspend fun getMaxDisplayOrder(cardId: Long): Int
}
