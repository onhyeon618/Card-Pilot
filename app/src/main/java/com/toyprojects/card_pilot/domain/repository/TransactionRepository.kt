package com.toyprojects.card_pilot.domain.repository

import com.toyprojects.card_pilot.model.Transaction
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth

interface TransactionRepository {
    suspend fun getTransactionById(transactionId: Long): Transaction?
    fun getTransactionsForBenefitByMonth(
        benefitId: Long,
        yearMonth: YearMonth
    ): Flow<List<Transaction>>

    suspend fun insertTransaction(transaction: Transaction, benefitId: Long)
    suspend fun updateTransaction(transaction: Transaction, benefitId: Long)
    suspend fun deleteTransaction(transactionId: Long)
    suspend fun deleteAllTransactions()
}
