package com.toyprojects.card_pilot.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.toyprojects.card_pilot.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions WHERE id = :transactionId")
    suspend fun getTransactionById(transactionId: Long): TransactionEntity?

    @Query("SELECT * FROM transactions WHERE benefitId = :benefitId AND dateTime >= :startDateTime AND dateTime < :endDateTime ORDER BY dateTime DESC")
    fun getTransactionsForBenefitByMonth(
        benefitId: Long,
        startDateTime: java.time.LocalDateTime,
        endDateTime: java.time.LocalDateTime
    ): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE benefitId = :benefitId AND dateTime >= :startDateTime AND dateTime < :endDateTime ORDER BY dateTime ASC")
    suspend fun getMonthlyTransactionsAsc(
        benefitId: Long,
        startDateTime: java.time.LocalDateTime,
        endDateTime: java.time.LocalDateTime
    ): List<TransactionEntity>

    @Query("SELECT * FROM transactions WHERE benefitId = :benefitId AND dateTime >= :startDateTime AND dateTime < :endDateTime ORDER BY dateTime DESC")
    suspend fun getMonthlyTransactionsDesc(
        benefitId: Long,
        startDateTime: java.time.LocalDateTime,
        endDateTime: java.time.LocalDateTime
    ): List<TransactionEntity>

    @Query("SELECT * FROM transactions WHERE benefitId = :benefitId AND merchant = :merchant AND dateTime = :dateTime AND amount = :amount LIMIT 1")
    suspend fun getTransactionByDetails(
        benefitId: Long,
        merchant: String,
        dateTime: java.time.LocalDateTime,
        amount: Long
    ): TransactionEntity?

    @Query("SELECT * FROM transactions WHERE benefitId = :benefitId")
    suspend fun getTransactionsForBenefitSync(benefitId: Long): List<TransactionEntity>

    @Insert
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Update
    suspend fun updateTransactions(transactions: List<TransactionEntity>)

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteTransactionById(id: Long)

    @Query("DELETE FROM transactions")
    suspend fun deleteAllTransactions()
}
