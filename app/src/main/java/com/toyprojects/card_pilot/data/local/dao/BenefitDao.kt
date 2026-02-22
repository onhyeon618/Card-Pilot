package com.toyprojects.card_pilot.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBenefit(benefit: BenefitEntity): Long

    @Update
    fun updateBenefits(benefits: List<BenefitEntity>)

    @Delete
    fun deleteBenefits(benefits: List<BenefitEntity>)

    @Query("SELECT MAX(displayOrder) FROM benefits WHERE cardId = :cardId")
    fun getMaxDisplayOrder(cardId: Long): Int?
}
