package com.toyprojects.card_pilot.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
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
               COALESCE((SELECT SUM(amount) FROM transactions t WHERE t.benefitId = b.id), 0) as usedAmount 
        FROM benefits b 
        WHERE b.cardId = :cardId 
        ORDER BY b.displayOrder ASC
    """
    )
    fun getBenefitListOfCard(cardId: Long): Flow<List<BenefitWithUsedAmount>>

    @Query("SELECT * FROM benefits WHERE cardId = :cardId")
    suspend fun getBenefitsOfCardSync(cardId: Long): List<BenefitEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBenefit(benefit: BenefitEntity)

    @Update
    fun updateBenefit(benefit: BenefitEntity)

    @Query("DELETE FROM benefits WHERE id = :id")
    suspend fun deleteBenefitById(id: Long)
}
