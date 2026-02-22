package com.toyprojects.card_pilot.data.repository

import com.toyprojects.card_pilot.data.local.dao.TransactionDao
import com.toyprojects.card_pilot.data.local.entity.TransactionEntity
import com.toyprojects.card_pilot.domain.repository.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class TransactionRepositoryImpl(
    private val transactionDao: TransactionDao
) : TransactionRepository {
    override fun getTransactionsForBenefitByMonth(
        benefitId: Long,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime
    ): Flow<List<TransactionEntity>> {
        return transactionDao.getTransactionsForBenefitByMonth(
            benefitId,
            startDateTime,
            endDateTime
        )
    }

    override suspend fun insertTransaction(transaction: TransactionEntity): Long =
        withContext(Dispatchers.IO) {
            transactionDao.insertTransaction(transaction)
        }

    override suspend fun updateTransaction(transaction: TransactionEntity) =
        withContext(Dispatchers.IO) {
            transactionDao.updateTransaction(transaction)
        }

    override suspend fun deleteTransaction(transaction: TransactionEntity) =
        withContext(Dispatchers.IO) {
            transactionDao.deleteTransaction(transaction)
        }
}
