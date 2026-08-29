package com.toyprojects.card_pilot.data.repository

import com.toyprojects.card_pilot.data.local.dao.TransactionDao
import com.toyprojects.card_pilot.data.local.entity.TransactionEntity
import com.toyprojects.card_pilot.domain.repository.TransactionRepository
import com.toyprojects.card_pilot.model.Transaction
import java.time.YearMonth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TransactionRepositoryImpl(
    private val transactionDao: TransactionDao
) : TransactionRepository {

    override suspend fun getTransactionById(transactionId: Long): Transaction? {
        return transactionDao.getTransactionById(transactionId)?.let { result ->
            Transaction(
                id = result.id,
                merchant = result.merchant,
                dateTime = result.dateTime,
                amount = result.amount,
                appliedAmount = result.appliedAmount
            )
        }
    }

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
                    amount = result.amount,
                    appliedAmount = result.appliedAmount
                )
            }
        }
    }

    override suspend fun insertTransaction(transaction: Transaction, benefitId: Long) {
        val entity = TransactionEntity(
            id = transaction.id,
            benefitId = benefitId,
            merchant = transaction.merchant,
            dateTime = transaction.dateTime,
            amount = transaction.amount,
            appliedAmount = transaction.appliedAmount
        )
        transactionDao.insertTransaction(entity)
    }

    override suspend fun updateTransaction(transaction: Transaction, benefitId: Long) {
        val entity = TransactionEntity(
            id = transaction.id,
            benefitId = benefitId,
            merchant = transaction.merchant,
            dateTime = transaction.dateTime,
            amount = transaction.amount,
            appliedAmount = transaction.appliedAmount
        )
        transactionDao.updateTransaction(entity)
    }

    override suspend fun deleteTransaction(transactionId: Long) {
        transactionDao.deleteTransactionById(transactionId)
    }

    override suspend fun deleteAllTransactions() {
        transactionDao.deleteAllTransactions()
    }
}
