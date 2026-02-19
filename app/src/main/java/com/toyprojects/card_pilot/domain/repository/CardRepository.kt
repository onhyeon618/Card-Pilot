package com.toyprojects.card_pilot.domain.repository

import com.toyprojects.card_pilot.model.CardInfo
import com.toyprojects.card_pilot.model.Benefit
import com.toyprojects.card_pilot.model.Transaction
import kotlinx.coroutines.flow.Flow

interface CardRepository {
    fun getAllCardsList(): Flow<List<CardInfo>>
    
    fun getSelectedCardDetail(id: Int): Flow<CardInfo?>
    
    suspend fun insertCard(card: CardInfo)
    
    suspend fun updateCard(card: CardInfo)
    
    suspend fun insertTransaction(cardId: Int, benefitName: String, transaction: Transaction)
    
    suspend fun insertBenefit(cardId: Int, benefit: Benefit)
}
