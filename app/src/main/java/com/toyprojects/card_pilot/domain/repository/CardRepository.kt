package com.toyprojects.card_pilot.domain.repository

import com.toyprojects.card_pilot.data.local.entity.CardInfoEntity
import com.toyprojects.card_pilot.data.local.relation.CardWithTotalAmount
import kotlinx.coroutines.flow.Flow

interface CardRepository {
    fun getAllCards(): Flow<List<CardInfoEntity>>
    fun getCardWithTotalAmount(cardId: Long): Flow<CardWithTotalAmount?>
    suspend fun insertCard(card: CardInfoEntity): Long
    suspend fun updateCard(card: CardInfoEntity)
    suspend fun deleteCard(card: CardInfoEntity)
    suspend fun getMaxDisplayOrder(): Int
}
