package com.toyprojects.card_pilot.data.repository

import com.toyprojects.card_pilot.data.local.dao.CardDao
import com.toyprojects.card_pilot.data.local.entity.CardInfoEntity
import com.toyprojects.card_pilot.data.local.relation.CardWithTotalAmount
import com.toyprojects.card_pilot.domain.repository.CardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class CardRepositoryImpl(
    private val cardDao: CardDao
) : CardRepository {
    override fun getAllCards(): Flow<List<CardInfoEntity>> {
        return cardDao.getAllCards()
    }

    override fun getCardWithTotalAmount(cardId: Long): Flow<CardWithTotalAmount?> {
        return cardDao.getCardWithTotalAmount(cardId)
    }

    override suspend fun insertCard(card: CardInfoEntity): Long = withContext(Dispatchers.IO) {
        cardDao.insertCard(card)
    }

    override suspend fun updateCard(card: CardInfoEntity) = withContext(Dispatchers.IO) {
        cardDao.updateCard(card)
    }

    override suspend fun deleteCard(card: CardInfoEntity) = withContext(Dispatchers.IO) {
        cardDao.deleteCard(card)
    }

    override suspend fun getMaxDisplayOrder(): Int = withContext(Dispatchers.IO) {
        cardDao.getMaxDisplayOrder() ?: -1
    }
}
