package com.toyprojects.card_pilot.domain.repository

import com.toyprojects.card_pilot.model.BenefitProperty
import com.toyprojects.card_pilot.model.CardInfo
import com.toyprojects.card_pilot.model.CardSimpleInfo
import kotlinx.coroutines.flow.Flow

interface CardRepository {
    fun getAllCards(): Flow<List<CardSimpleInfo>>
    suspend fun getCardById(cardId: Long): CardSimpleInfo?
    fun getCardWithTotalAmount(cardId: Long, yearMonth: java.time.YearMonth): Flow<CardInfo?>
    suspend fun insertCard(card: CardInfo, benefits: List<BenefitProperty>): Long
    suspend fun updateCard(card: CardInfo, benefits: List<BenefitProperty>)
    suspend fun updateCardOrders(cards: List<CardSimpleInfo>)
    suspend fun deleteCard(cardId: Long)
    suspend fun deleteAllCards()
    suspend fun getMaxDisplayOrder(): Int
}
