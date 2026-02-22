package com.toyprojects.card_pilot.data.repository

import com.toyprojects.card_pilot.data.local.dao.BenefitDao
import com.toyprojects.card_pilot.data.local.entity.BenefitEntity
import com.toyprojects.card_pilot.data.local.relation.BenefitWithUsedAmount
import com.toyprojects.card_pilot.domain.repository.BenefitRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class BenefitRepositoryImpl(
    private val benefitDao: BenefitDao
) : BenefitRepository {
    override fun getBenefitListOfCard(cardId: Long): Flow<List<BenefitWithUsedAmount>> {
        return benefitDao.getBenefitListOfCard(cardId)
    }

    override suspend fun insertBenefit(benefit: BenefitEntity): Long = withContext(Dispatchers.IO) {
        benefitDao.insertBenefit(benefit)
    }

    override suspend fun updateBenefits(benefits: List<BenefitEntity>) =
        withContext(Dispatchers.IO) {
            benefitDao.updateBenefits(benefits)
        }

    override suspend fun deleteBenefits(benefits: List<BenefitEntity>) =
        withContext(Dispatchers.IO) {
            benefitDao.deleteBenefits(benefits)
        }

    override suspend fun getMaxDisplayOrder(cardId: Long): Int = withContext(Dispatchers.IO) {
        benefitDao.getMaxDisplayOrder(cardId) ?: -1
    }
}
