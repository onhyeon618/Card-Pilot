package com.toyprojects.card_pilot.domain.repository

import com.toyprojects.card_pilot.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

import java.time.LocalDateTime

interface TransactionRepository {
    fun getTransactionsForBenefitByMonth(
        benefitId: Long,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime
    ): Flow<List<TransactionEntity>>

    suspend fun insertTransaction(transaction: TransactionEntity): Long
    suspend fun updateTransaction(transaction: TransactionEntity)
    suspend fun deleteTransaction(transaction: TransactionEntity)
}
