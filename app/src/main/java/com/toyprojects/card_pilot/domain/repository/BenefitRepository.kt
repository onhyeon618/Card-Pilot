package com.toyprojects.card_pilot.domain.repository

import com.toyprojects.card_pilot.model.Benefit
import com.toyprojects.card_pilot.model.BenefitProperty
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth

interface BenefitRepository {
    suspend fun getBenefitPropertyById(benefitId: Long): BenefitProperty?
    suspend fun getBenefitsOfCardSync(cardId: Long): List<Benefit>
    fun getBenefitWithUsage(benefitId: Long, yearMonth: YearMonth): Flow<Benefit?>
}
