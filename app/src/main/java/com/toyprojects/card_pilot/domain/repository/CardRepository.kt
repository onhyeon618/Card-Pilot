package com.toyprojects.card_pilot.domain.repository

import com.toyprojects.card_pilot.model.CardInfo
import com.toyprojects.card_pilot.model.CardSimpleInfo
import kotlinx.coroutines.flow.Flow

interface CardRepository {
    fun getAllCards(): Flow<List<CardSimpleInfo>>
    fun getCardWithTotalAmount(cardId: Long): Flow<CardInfo?>
    suspend fun insertCard(card: CardInfo): Long
    suspend fun updateCard(card: CardInfo)
    suspend fun updateCardOrders(cards: List<CardSimpleInfo>)
    suspend fun deleteCard(cardId: Long)
    suspend fun getMaxDisplayOrder(): Int
}
