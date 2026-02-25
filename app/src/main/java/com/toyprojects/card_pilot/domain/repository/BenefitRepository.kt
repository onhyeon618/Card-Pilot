package com.toyprojects.card_pilot.domain.repository

import com.toyprojects.card_pilot.model.Benefit
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth

interface BenefitRepository {
    fun getBenefitWithUsage(benefitId: Long, yearMonth: YearMonth): Flow<Benefit?>
}
