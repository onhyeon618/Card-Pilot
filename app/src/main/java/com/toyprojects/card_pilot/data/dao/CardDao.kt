package com.toyprojects.card_pilot.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction // Room Transaction
import com.toyprojects.card_pilot.data.entity.CardEntity
import com.toyprojects.card_pilot.data.relation.CardWithBenefits
import com.toyprojects.card_pilot.data.relation.CardWithFullDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: CardEntity): Long

    @Delete
    suspend fun deleteCard(card: CardEntity)

    @Query("SELECT * FROM cards")
    fun getAllCards(): Flow<List<CardEntity>>

    @Transaction // Important for @Relation
    @Query("SELECT * FROM cards")
    fun getCardsWithBenefits(): Flow<List<CardWithBenefits>>

    @Transaction
    @Query("SELECT * FROM cards")
    fun getCardsWithFullDetails(): Flow<List<CardWithFullDetails>>

    @Query("SELECT * FROM cards WHERE id = :cardId")
    suspend fun getCardById(cardId: Int): CardEntity?
}
