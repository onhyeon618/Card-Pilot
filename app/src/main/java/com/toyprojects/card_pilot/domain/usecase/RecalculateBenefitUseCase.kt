package com.toyprojects.card_pilot.domain.usecase

import com.toyprojects.card_pilot.domain.repository.BenefitRepository
import com.toyprojects.card_pilot.domain.repository.TransactionRepository
import com.toyprojects.card_pilot.model.BenefitProperty
import com.toyprojects.card_pilot.model.Transaction
import java.time.YearMonth
import kotlin.math.roundToLong

class RecalculateBenefitUseCase(
    private val transactionRepository: TransactionRepository,
    private val benefitRepository: BenefitRepository
) {
    suspend operator fun invoke(benefitId: Long, yearMonth: YearMonth) {
        val benefitProperty = benefitRepository.getBenefitPropertyById(benefitId)
            ?: throw IllegalArgumentException("Benefit with ID $benefitId not found")
        val existingTransactions = transactionRepository.getMonthlyTransactionsAsc(benefitId, yearMonth)

        var monthBenefitConsumed = 0L
        val dailyAppliedSum = mutableMapOf<java.time.LocalDate, Long>()
        val transactionsToUpdate = mutableListOf<Transaction>()

        for (transaction in existingTransactions) {
            val transactionDate = transaction.dateTime.toLocalDate()
            val todayApplied = dailyAppliedSum.getOrDefault(transactionDate, 0L)

            val appliedAmount = calculateAppliedAmount(transaction, benefitProperty, monthBenefitConsumed, todayApplied)

            monthBenefitConsumed += (appliedAmount * (benefitProperty.rate / 100.0)).toLong()
            dailyAppliedSum[transactionDate] = todayApplied + appliedAmount

            if (transaction.appliedAmount != appliedAmount) {
                transactionsToUpdate.add(transaction.copy(appliedAmount = appliedAmount))
            }
        }

        if (transactionsToUpdate.isNotEmpty()) {
            transactionRepository.updateTransactions(transactionsToUpdate, benefitProperty.id)
        }
    }

    private fun calculateAppliedAmount(
        transaction: Transaction,
        benefitProperty: BenefitProperty,
        monthBenefitConsumed: Long,
        todayApplied: Long
    ): Long {
        var limit = Long.MAX_VALUE

        // 1. 이번달 잔여 혜택 (혜택 제공량 기반)
        if (benefitProperty.capAmount > 0) {
            val remainingBenefit = maxOf(0L, benefitProperty.capAmount - monthBenefitConsumed)
            if (benefitProperty.rate > 0f) {
                val remainingEligible = (remainingBenefit * 100.0 / benefitProperty.rate).roundToLong()
                limit = minOf(limit, remainingEligible)
            }
        }

        // 2. 일일 제한이 있는 경우, 일별 잔여 혜택과 비교
        if (benefitProperty.dailyLimit != null && benefitProperty.dailyLimit > 0) {
            val remainingDaily = maxOf(0L, benefitProperty.dailyLimit - todayApplied)
            limit = minOf(limit, remainingDaily)
        }

        // 3. 1회 제한이 있는 경우, 1회 최대 혜택과 비교
        if (benefitProperty.oneTimeLimit != null && benefitProperty.oneTimeLimit > 0) {
            limit = minOf(limit, benefitProperty.oneTimeLimit)
        }

        // 4. 실제 지출 금액과 한도 금액 비교
        return minOf(transaction.amount, limit)
    }
}
