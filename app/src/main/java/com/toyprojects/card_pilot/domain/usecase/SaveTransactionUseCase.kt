package com.toyprojects.card_pilot.domain.usecase

import com.toyprojects.card_pilot.domain.repository.DatabaseTransactionRunner
import com.toyprojects.card_pilot.domain.repository.NotificationRepository
import com.toyprojects.card_pilot.domain.repository.TransactionRepository
import com.toyprojects.card_pilot.model.BenefitProperty
import com.toyprojects.card_pilot.model.Transaction
import java.time.LocalDateTime
import java.time.YearMonth

class SaveTransactionUseCase(
    private val transactionRepository: TransactionRepository,
    private val notificationRepository: NotificationRepository,
    private val recalculateBenefitUseCase: RecalculateBenefitUseCase,
    private val transactionRunner: DatabaseTransactionRunner
) {
    suspend operator fun invoke(
        transactionId: Long?,
        merchant: String,
        dateTime: LocalDateTime,
        amount: Long,
        benefitProperty: BenefitProperty,
        notificationId: Long?
    ) {
        val targetDate = dateTime.toLocalDate()
        val yearMonth = YearMonth.from(targetDate)

        val transaction = Transaction(
            id = transactionId ?: 0L,
            merchant = merchant,
            dateTime = dateTime,
            amount = amount,
            appliedAmount = 0L // 추후 RecalculateBenefitUseCase가 정확한 값으로 업데이트 함
        )

        transactionRunner {
            var oldYearMonth: YearMonth? = null
            if (transactionId != null) {
                val existingTransaction = transactionRepository.getTransactionById(transactionId)
                if (existingTransaction != null) {
                    oldYearMonth = YearMonth.from(existingTransaction.dateTime.toLocalDate())
                }
                transactionRepository.updateTransaction(transaction, benefitProperty.id)
            } else {
                transactionRepository.insertTransaction(transaction, benefitProperty.id)
            }

            // 변경 전 월과 변경 후 월이 다를 경우, 이전 월도 재계산
            if (oldYearMonth != null && oldYearMonth != yearMonth) {
                recalculateBenefitUseCase(benefitProperty.id, oldYearMonth)
            }

            // 해당 월의 전체 내역을 다시 불러와서 혜택 한도를 연쇄적으로 재계산 및 업데이트
            recalculateBenefitUseCase(benefitProperty.id, yearMonth)

            if (notificationId != null) {
                notificationRepository.deleteNotificationById(notificationId)
            }
        }
    }
}
