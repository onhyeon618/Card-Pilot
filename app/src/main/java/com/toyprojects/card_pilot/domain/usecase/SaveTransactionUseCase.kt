package com.toyprojects.card_pilot.domain.usecase

import com.toyprojects.card_pilot.domain.repository.NotificationRepository
import com.toyprojects.card_pilot.domain.repository.TransactionRepository
import com.toyprojects.card_pilot.model.BenefitProperty
import com.toyprojects.card_pilot.model.Transaction
import java.time.LocalDateTime
import java.time.YearMonth

class SaveTransactionUseCase(
    private val transactionRepository: TransactionRepository,
    private val notificationRepository: NotificationRepository
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

        val transactions = transactionRepository.getTransactionsForBenefitByMonthSync(benefitProperty.id, yearMonth)

        var monthAppliedSum = 0L
        var todayAppliedSum = 0L

        for (t in transactions) {
            // 지출 내역을 수정 중인 경우, 현재 내역의 적용 금액은 계산에서 제외
            if (transactionId != null && t.id == transactionId) {
                continue
            }

            monthAppliedSum += t.appliedAmount
            if (t.dateTime.toLocalDate() == targetDate) {
                todayAppliedSum += t.appliedAmount
            }
        }

        // 1. 이번달 잔여 혜택
        var limit = maxOf(0L, benefitProperty.capAmount - monthAppliedSum)

        // 2. 일일 제한이 있는 경우, 일별 잔여 혜택과 비교
        if (benefitProperty.dailyLimit != null) {
            limit = minOf(limit, maxOf(0L, benefitProperty.dailyLimit - todayAppliedSum))
        }

        // 3. 1회 제한이 있는 경우, 1회 최대 혜택과 비교
        if (benefitProperty.oneTimeLimit != null) {
            limit = minOf(limit, benefitProperty.oneTimeLimit)
        }

        // 4. 실제 지출 금액과 한도 금액 비교
        val appliedAmount = minOf(amount, limit)

        val transaction = Transaction(
            id = transactionId ?: 0L,
            merchant = merchant,
            dateTime = dateTime,
            amount = amount,
            appliedAmount = appliedAmount
        )

        if (transactionId != null) {
            transactionRepository.updateTransaction(transaction, benefitProperty.id)
        } else {
            transactionRepository.insertTransaction(transaction, benefitProperty.id)
        }

        if (notificationId != null) {
            notificationRepository.deleteNotificationById(notificationId)
        }
    }
}
