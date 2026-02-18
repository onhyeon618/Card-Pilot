package com.toyprojects.card_pilot.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.toyprojects.card_pilot.data.entity.BenefitEntity
import com.toyprojects.card_pilot.data.relation.BenefitWithTransactions
import kotlinx.coroutines.flow.Flow

@Dao
interface BenefitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBenefit(benefit: BenefitEntity): Long

    @Delete
    suspend fun deleteBenefit(benefit: BenefitEntity)

    @Query("SELECT * FROM benefits WHERE cardId = :cardId")
    fun getBenefitsByCardId(cardId: Int): Flow<List<BenefitEntity>>

    @Transaction
    @Query("SELECT * FROM benefits WHERE id = :benefitId")
    fun getBenefitWithTransactions(benefitId: Int): Flow<BenefitWithTransactions?>
}
