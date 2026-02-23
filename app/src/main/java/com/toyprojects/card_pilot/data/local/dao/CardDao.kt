package com.toyprojects.card_pilot.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.toyprojects.card_pilot.data.local.entity.CardInfoEntity
import com.toyprojects.card_pilot.data.local.relation.CardWithTotalAmount
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    @Query("SELECT * FROM cards ORDER BY displayOrder ASC")
    fun getAllCards(): Flow<List<CardInfoEntity>>

    @Query(
        """
        SELECT c.*, 
               COALESCE((
                   SELECT SUM(t.amount) 
                   FROM transactions t 
                   INNER JOIN benefits b ON t.benefitId = b.id 
                   WHERE b.cardId = :cardId
               ), 0) as totalAmount 
        FROM cards c 
        WHERE c.id = :cardId
    """
    )
    fun getCardWithTotalAmount(cardId: Long): Flow<CardWithTotalAmount?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCard(card: CardInfoEntity): Long

    @Update
    fun updateCard(card: CardInfoEntity)

    @Query("UPDATE cards SET displayOrder = :displayOrder WHERE id = :cardId")
    suspend fun updateCardDisplayOrder(cardId: Long, displayOrder: Int)

    @Query("DELETE FROM cards WHERE id = :id")
    suspend fun deleteCardById(id: Long)

    @Query("SELECT MAX(displayOrder) FROM cards")
    fun getMaxDisplayOrder(): Int?
}
