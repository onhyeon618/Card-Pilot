package com.toyprojects.card_pilot.data.repository

import com.toyprojects.card_pilot.data.local.dao.BenefitDao
import com.toyprojects.card_pilot.domain.repository.BenefitRepository
import com.toyprojects.card_pilot.mock.MockData
import com.toyprojects.card_pilot.model.Benefit
import com.toyprojects.card_pilot.model.BenefitProperty
import com.toyprojects.card_pilot.model.BenefitSimpleInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.YearMonth

class BenefitRepositoryImpl(
    private val benefitDao: BenefitDao
) : BenefitRepository {
    override suspend fun getBenefitPropertyById(benefitId: Long): BenefitProperty? {
//        return benefitDao.getBenefitById(benefitId)?.let { result ->
//            BenefitProperty(
//                id = result.id,
//                name = result.name,
//                explanation = result.explanation,
//                capAmount = result.capAmount,
//                dailyLimit = result.dailyLimit,
//                oneTimeLimit = result.oneTimeLimit,
//                rate = result.rate
//            )
//        }
        return MockData.sampleBenefitProperty
    }

    override suspend fun getBenefitPropertiesOfCardSync(cardId: Long): List<BenefitProperty> {
//        return benefitDao.getBenefitsOfCardSync(cardId).map { result ->
//            BenefitProperty(
//                id = result.id,
//                name = result.name,
//                explanation = result.explanation,
//                capAmount = result.capAmount,
//                dailyLimit = result.dailyLimit,
//                oneTimeLimit = result.oneTimeLimit,
//                rate = result.rate
//            )
//        }
        return listOf(MockData.sampleBenefitProperty)
    }

    override suspend fun getSimpleBenefitsOfCardSync(cardId: Long): List<BenefitSimpleInfo> {
//        return benefitDao.getSimpleBenefitsOfCardSync(cardId).map { entity ->
//            BenefitSimpleInfo(
//                id = entity.id,
//                name = entity.name
//            )
//        }
        return MockData.sampleBenefitPropertyList.map { benefitProperty ->
            BenefitSimpleInfo(
                id = benefitProperty.id,
                name = benefitProperty.name
            )
        }
    }

    override fun getBenefitWithUsage(benefitId: Long, yearMonth: YearMonth): Flow<Benefit?> {
        // val startDateTime = yearMonth.atDay(1).atStartOfDay()
        // val endDateTime = yearMonth.plusMonths(1).atDay(1).atStartOfDay()

        // return benefitDao.getBenefitWithUsedAmount(benefitId, startDateTime, endDateTime).map { result ->
        //     result?.let { benefit ->
        //         Benefit(
        //             id = benefit.benefit.id,
        //             name = benefit.benefit.name,
        //             explanation = benefit.benefit.explanation,
        //             capAmount = benefit.benefit.capAmount,
        //             usedAmount = benefit.usedAmount,
        //             displayOrder = benefit.benefit.displayOrder
        //         )
        //     }
        // }
        val benefit = MockData.mockCardDetails.values
            .flatMap { it.benefits }
            .find { it.id == benefitId }
        return flowOf(benefit)
    }

    override suspend fun deleteAllBenefits() {
//        benefitDao.deleteAllBenefits()
    }
}
