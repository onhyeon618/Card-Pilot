package com.toyprojects.card_pilot.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.toyprojects.card_pilot.data.local.entity.CardInfoEntity
import com.toyprojects.card_pilot.data.local.entity.CardOrderUpdate
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
                   AND t.dateTime >= :startDateTime 
                   AND t.dateTime < :endDateTime
               ), 0) as totalAmount 
        FROM cards c 
        WHERE c.id = :cardId
    """
    )
    fun getCardWithTotalAmount(
        cardId: Long,
        startDateTime: java.time.LocalDateTime,
        endDateTime: java.time.LocalDateTime
    ): Flow<CardWithTotalAmount?>

    @Insert
    suspend fun insertCard(card: CardInfoEntity): Long

    @Update
    suspend fun updateCard(card: CardInfoEntity)

    @Update(entity = CardInfoEntity::class)
    suspend fun updateCardDisplayOrders(updates: List<CardOrderUpdate>)

    @Query("DELETE FROM cards WHERE id = :id")
    suspend fun deleteCardById(id: Long)

    @Query("SELECT MAX(displayOrder) FROM cards")
    suspend fun getMaxDisplayOrder(): Int?
}
