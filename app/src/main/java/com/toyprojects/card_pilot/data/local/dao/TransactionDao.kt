package com.toyprojects.card_pilot.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.toyprojects.card_pilot.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions WHERE benefitId = :benefitId AND dateTime >= :startDateTime AND dateTime < :endDateTime ORDER BY dateTime DESC")
    fun getTransactionsForBenefitByMonth(
        benefitId: Long,
        startDateTime: java.time.LocalDateTime,
        endDateTime: java.time.LocalDateTime
    ): Flow<List<TransactionEntity>>

    @Insert
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteTransactionById(id: Long)
}
