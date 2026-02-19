package com.toyprojects.card_pilot.data.repository

import com.toyprojects.card_pilot.data.dao.BenefitDao
import com.toyprojects.card_pilot.data.dao.CardDao
import com.toyprojects.card_pilot.data.dao.TransactionDao
import com.toyprojects.card_pilot.data.entity.BenefitEntity
import com.toyprojects.card_pilot.data.entity.CardEntity
import com.toyprojects.card_pilot.data.entity.TransactionEntity
import com.toyprojects.card_pilot.data.relation.BenefitWithTransactions
import com.toyprojects.card_pilot.data.relation.CardWithFullDetails
import com.toyprojects.card_pilot.domain.repository.CardRepository
import com.toyprojects.card_pilot.model.Benefit
import com.toyprojects.card_pilot.model.CardInfo
import com.toyprojects.card_pilot.model.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CardRepositoryImpl(
    private val cardDao: CardDao,
    private val benefitDao: BenefitDao,
    private val transactionDao: TransactionDao
) : CardRepository {

    override fun getAllCardsList(): Flow<List<CardInfo>> =
        cardDao.getAllCards().map { entities ->
            entities.map { it.toDomainModel() }
        }

    override fun getSelectedCardDetail(id: Int): Flow<CardInfo?> =
        cardDao.getCardsWithFullDetails().map { entities ->
            entities.find { it.card.id == id }?.toDomainModel()
        }

    override suspend fun insertCard(card: CardInfo) {
        val cardId = cardDao.insertCard(
            CardEntity(
                name = card.name,
                image = card.image
            )
        )
        card.benefits.forEach { benefit ->
            insertBenefit(cardId.toInt(), benefit)
        }
    }

    override suspend fun updateCard(card: CardInfo) {
        cardDao.updateCard(
            CardEntity(
                id = card.id,
                name = card.name,
                image = card.image
            )
        )
        
        val existingBenefits = benefitDao.getBenefitsListByCardId(card.id)
        val newBenefitNames = card.benefits.map { it.name }.toSet()
        
        // 1. Delete removed benefits
        existingBenefits.forEach { existing ->
            if (existing.name !in newBenefitNames) {
                benefitDao.deleteBenefitByName(card.id, existing.name)
            }
        }
        
        // 2. Upsert current benefits
        card.benefits.forEach { benefit ->
            insertBenefit(card.id, benefit)
        }
    }

    override suspend fun insertTransaction(cardId: Int, benefitName: String, transaction: Transaction) {
        transactionDao.insertTransaction(
            TransactionEntity(
                cardId = cardId,
                benefitName = benefitName,
                name = transaction.name,
                date = transaction.date,
                time = transaction.time,
                amount = transaction.amount.toDouble()
            )
        )
    }

    override suspend fun insertBenefit(cardId: Int, benefit: Benefit) {
        benefitDao.insertBenefit(
            BenefitEntity(
                cardId = cardId,
                name = benefit.name,
                usedAmount = benefit.usedAmount.toDouble(),
                totalAmount = benefit.totalAmount.toDouble(),
                eligiblePerUse = benefit.eligiblePerUse?.toDouble(),
                explanation = benefit.explanation
            )
        )
    }

    // Mappers
    private fun CardEntity.toDomainModel(): CardInfo {
        return CardInfo(
            id = id,
            name = name,
            image = image,
            benefits = emptyList()
        )
    }
    
    private fun CardWithFullDetails.toDomainModel(): CardInfo {
        return CardInfo(
            id = card.id,
            name = card.name,
            image = card.image,
            benefits = benefits.map { it.toDomainModel() }
        )
    }

    private fun BenefitWithTransactions.toDomainModel(): Benefit {
        return Benefit(
            transactions = transactions.map { it.toDomainModel() }
        )

    }

    private fun TransactionEntity.toDomainModel(): Transaction {
        return Transaction(
            id = id,
            name = name,
            date = date,
            time = time,
            amount = amount.toLong()
        )
    }
}
