package com.toyprojects.card_pilot.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.toyprojects.card_pilot.data.local.entity.BenefitEntity
import com.toyprojects.card_pilot.data.local.entity.BenefitSimpleEntity
import com.toyprojects.card_pilot.data.local.relation.BenefitWithUsedAmount
import kotlinx.coroutines.flow.Flow

@Dao
interface BenefitDao {
    @Query("SELECT * FROM benefits WHERE id = :benefitId")
    suspend fun getBenefitById(benefitId: Long): BenefitEntity?

    @Query("SELECT * FROM benefits WHERE cardId = :cardId AND name = :name LIMIT 1")
    suspend fun getBenefitByName(cardId: Long, name: String): BenefitEntity?

    @Query(
        """
        SELECT b.*, 
               COALESCE(SUM(CAST(t.appliedAmount * (b.rate / 100.0) AS INTEGER)), 0) as usedBenefitAmount,
               COALESCE(SUM(t.appliedAmount), 0) as usedPaymentAmount
        FROM benefits b 
        LEFT JOIN transactions t ON t.benefitId = b.id 
            AND t.dateTime >= :startDateTime 
            AND t.dateTime < :endDateTime
        WHERE b.cardId = :cardId 
        GROUP BY b.id
        ORDER BY b.displayOrder ASC
    """
    )
    fun getBenefitsOfCard(
        cardId: Long,
        startDateTime: java.time.LocalDateTime,
        endDateTime: java.time.LocalDateTime
    ): Flow<List<BenefitWithUsedAmount>>

    @Query("SELECT * FROM benefits WHERE cardId = :cardId ORDER BY displayOrder ASC")
    suspend fun getBenefitsOfCardList(cardId: Long): List<BenefitEntity>

    @Query("SELECT id, name FROM benefits WHERE cardId = :cardId ORDER BY displayOrder ASC")
    suspend fun getSimpleBenefitsOfCardList(cardId: Long): List<BenefitSimpleEntity>

    @Query(
        """
        SELECT b.*, 
               COALESCE(SUM(CAST(t.appliedAmount * (b.rate / 100.0) AS INTEGER)), 0) as usedBenefitAmount,
               COALESCE(SUM(t.appliedAmount), 0) as usedPaymentAmount
        FROM benefits b 
        LEFT JOIN transactions t ON t.benefitId = b.id 
            AND t.dateTime >= :startDateTime 
            AND t.dateTime < :endDateTime
        WHERE b.id = :benefitId
        GROUP BY b.id
    """
    )
    fun getBenefitWithUsedAmount(
        benefitId: Long,
        startDateTime: java.time.LocalDateTime,
        endDateTime: java.time.LocalDateTime
    ): Flow<BenefitWithUsedAmount?>

    @Insert
    suspend fun insertBenefits(benefits: List<BenefitEntity>)

    @Update
    suspend fun updateBenefits(benefits: List<BenefitEntity>)

    @Query("DELETE FROM benefits WHERE id IN (:ids)")
    suspend fun deleteBenefitsByIds(ids: List<Long>)

    @Query("DELETE FROM benefits")
    suspend fun deleteAllBenefits()
}
