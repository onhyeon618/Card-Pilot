package com.toyprojects.card_pilot.domain.repository

import com.toyprojects.card_pilot.model.Benefit
import com.toyprojects.card_pilot.model.BenefitProperty
import com.toyprojects.card_pilot.model.BenefitSimpleInfo
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth

interface BenefitRepository {
    suspend fun getBenefitPropertyById(benefitId: Long): BenefitProperty?
    suspend fun getBenefitPropertiesOfCardSync(cardId: Long): List<BenefitProperty>
    suspend fun getSimpleBenefitsOfCardSync(cardId: Long): List<BenefitSimpleInfo>
    fun getBenefitWithUsage(benefitId: Long, yearMonth: YearMonth): Flow<Benefit?>
    suspend fun deleteAllBenefits()
}
