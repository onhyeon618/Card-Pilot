package com.toyprojects.card_pilot.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.toyprojects.card_pilot.data.local.entity.BenefitEntity
import com.toyprojects.card_pilot.data.local.relation.BenefitWithUsedAmount
import kotlinx.coroutines.flow.Flow

@Dao
interface BenefitDao {
    @Query(
        """
        SELECT b.*, 
               COALESCE((
                   SELECT SUM(amount) 
                   FROM transactions t 
                   WHERE t.benefitId = b.id
                   AND t.dateTime >= :startDateTime 
                   AND t.dateTime < :endDateTime
               ), 0) as usedAmount 
        FROM benefits b 
        WHERE b.cardId = :cardId 
        ORDER BY b.displayOrder ASC
    """
    )
    fun getBenefitsOfCard(
        cardId: Long,
        startDateTime: java.time.LocalDateTime,
        endDateTime: java.time.LocalDateTime
    ): Flow<List<BenefitWithUsedAmount>>

    @Query("SELECT * FROM benefits WHERE cardId = :cardId")
    suspend fun getBenefitsOfCardSync(cardId: Long): List<BenefitEntity>

    @Query(
        """
        SELECT b.*, 
               COALESCE((
                   SELECT SUM(amount) 
                   FROM transactions t 
                   WHERE t.benefitId = b.id
                   AND t.dateTime >= :startDateTime 
                   AND t.dateTime < :endDateTime
               ), 0) as usedAmount 
        FROM benefits b 
        WHERE b.id = :benefitId
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
}
