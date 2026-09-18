package com.toyprojects.card_pilot.domain.usecase

import com.toyprojects.card_pilot.domain.repository.DatabaseTransactionRunner
import com.toyprojects.card_pilot.domain.repository.TransactionRepository
import java.time.YearMonth

class DeleteTransactionUseCase(
    private val transactionRepository: TransactionRepository,
    private val recalculateBenefitUseCase: RecalculateBenefitUseCase,
    private val transactionRunner: DatabaseTransactionRunner
) {
    suspend operator fun invoke(transactionId: Long, benefitId: Long) {
        transactionRunner {
            val transaction = transactionRepository.getTransactionById(transactionId) ?: return@transactionRunner
            val targetDate = transaction.dateTime.toLocalDate()
            val yearMonth = YearMonth.from(targetDate)

            transactionRepository.deleteTransaction(transactionId)

            recalculateBenefitUseCase(benefitId, yearMonth)
        }
    }
}
