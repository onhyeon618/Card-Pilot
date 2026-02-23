package com.toyprojects.card_pilot.data.repository

import com.toyprojects.card_pilot.data.local.dao.TransactionDao
import com.toyprojects.card_pilot.data.local.entity.TransactionEntity
import com.toyprojects.card_pilot.domain.repository.TransactionRepository
import com.toyprojects.card_pilot.model.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.YearMonth

class TransactionRepositoryImpl(
    private val transactionDao: TransactionDao
) : TransactionRepository {
    override fun getTransactionsForBenefitByMonth(
        benefitId: Long,
        yearMonth: YearMonth
    ): Flow<List<Transaction>> {
        val startDateTime = yearMonth.atDay(1).atStartOfDay()
        val endDateTime = yearMonth.plusMonths(1).atDay(1).atStartOfDay()

        return transactionDao.getTransactionsForBenefitByMonth(
            benefitId,
            startDateTime,
            endDateTime
        ).map { results ->
            results.map { result ->
                Transaction(
                    id = result.id,
                    merchant = result.merchant,
                    dateTime = result.dateTime,
                    amount = result.amount
                )
            }
        }
    }

    override suspend fun insertTransaction(transaction: Transaction, benefitId: Long) =
        withContext(Dispatchers.IO) {
            val entity = TransactionEntity(
                id = transaction.id,
                benefitId = benefitId,
                merchant = transaction.merchant,
                dateTime = transaction.dateTime,
                amount = transaction.amount
            )
            transactionDao.insertTransaction(entity)
        }

    override suspend fun updateTransaction(transaction: Transaction, benefitId: Long) =
        withContext(Dispatchers.IO) {
            val entity = TransactionEntity(
                id = transaction.id,
                benefitId = benefitId,
                merchant = transaction.merchant,
                dateTime = transaction.dateTime,
                amount = transaction.amount
            )
            transactionDao.updateTransaction(entity)
        }

    override suspend fun deleteTransaction(transactionId: Long) =
        withContext(Dispatchers.IO) {
            transactionDao.deleteTransactionById(transactionId)
        }
}
