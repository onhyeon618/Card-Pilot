package com.toyprojects.card_pilot.data.repository

import androidx.room.withTransaction
import com.toyprojects.card_pilot.data.local.AppDatabase
import com.toyprojects.card_pilot.data.local.dao.BenefitDao
import com.toyprojects.card_pilot.data.local.dao.CardDao
import com.toyprojects.card_pilot.data.local.entity.BenefitEntity
import com.toyprojects.card_pilot.data.local.entity.CardInfoEntity
import com.toyprojects.card_pilot.domain.repository.CardRepository
import com.toyprojects.card_pilot.model.Benefit
import com.toyprojects.card_pilot.model.CardInfo
import com.toyprojects.card_pilot.model.CardSimpleInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class CardRepositoryImpl(
    private val cardDao: CardDao,
    private val benefitDao: BenefitDao,
    private val database: AppDatabase
) : CardRepository {
    override fun getAllCards(): Flow<List<CardSimpleInfo>> {
        return cardDao.getAllCards().map { results ->
            results.map { result ->
                CardSimpleInfo(
                    id = result.id,
                    name = result.name,
                    image = result.image,
                    displayOrder = result.displayOrder
                )
            }
        }
    }

    override fun getCardWithTotalAmount(cardId: Long): Flow<CardInfo?> {
        val cardFlow = cardDao.getCardWithTotalAmount(cardId)
        val benefitsFlow = benefitDao.getBenefitListOfCard(cardId)

        return cardFlow.combine(benefitsFlow) { cardResult, benefitsResult ->
            cardResult?.let { card ->
                CardInfo(
                    id = card.card.id,
                    name = card.card.name,
                    image = card.card.image,
                    usageAmount = card.totalAmount,
                    benefits = benefitsResult.map { benefit ->
                        Benefit(
                            id = benefit.benefit.id,
                            name = benefit.benefit.name,
                            explanation = benefit.benefit.explanation,
                            capAmount = benefit.benefit.capAmount,
                            usedAmount = benefit.usedAmount,
                            displayOrder = benefit.benefit.displayOrder
                        )
                    },
                    displayOrder = card.card.displayOrder
                )
            }
        }
    }

    override suspend fun insertCard(card: CardInfo): Long = withContext(Dispatchers.IO) {
        database.withTransaction {
            val nextDisplayOrder = (cardDao.getMaxDisplayOrder() ?: -1) + 1

            val entity = CardInfoEntity(
                name = card.name,
                image = card.image,
                displayOrder = nextDisplayOrder
            )
            val newCardId = cardDao.insertCard(entity)

            // 혜택 데이터도 추가
            card.benefits.forEach { benefit ->
                val benefitEntity = BenefitEntity(
                    cardId = newCardId,
                    name = benefit.name,
                    capAmount = benefit.capAmount,
                    explanation = benefit.explanation,
                    displayOrder = benefit.displayOrder
                )
                benefitDao.insertBenefit(benefitEntity)
            }
            newCardId
        }
    }

    override suspend fun updateCard(card: CardInfo) = withContext(Dispatchers.IO) {
        database.withTransaction {
            val entity = CardInfoEntity(
                id = card.id,
                name = card.name,
                image = card.image,
                displayOrder = card.displayOrder
            )
            cardDao.updateCard(entity)

            // 기존 혜택 목록
            val originalBenefits = benefitDao.getBenefitsOfCardSync(card.id)

            // 새 혜택 목록에 없는 기존 혜택 삭제
            // 반드시 삭제 먼저 수행해야 UNIQUE 제약 조건(cardId, name) 충돌 방지 가능
            val remainingBenefitIds = card.benefits.map { it.id }.toSet()
            originalBenefits.forEach { if (it.id !in remainingBenefitIds) benefitDao.deleteBenefitById(it.id) }

            card.benefits.forEach { benefit ->
                val benefitEntity = BenefitEntity(
                    id = benefit.id,
                    cardId = card.id,
                    name = benefit.name,
                    capAmount = benefit.capAmount,
                    explanation = benefit.explanation,
                    displayOrder = benefit.displayOrder
                )

                if (benefit.id == 0L) {
                    // 새로 추가된 혜택
                    benefitDao.insertBenefit(benefitEntity)
                } else {
                    // 기존 혜택 - 업데이트
                    benefitDao.updateBenefit(benefitEntity)
                }
            }
        }
    }

    override suspend fun updateCardOrders(cards: List<CardSimpleInfo>) = withContext(Dispatchers.IO) {
        database.withTransaction {
            cards.forEach { card ->
                cardDao.updateCardDisplayOrder(card.id, card.displayOrder)
            }
        }
    }

    override suspend fun deleteCard(cardId: Long) = withContext(Dispatchers.IO) {
        database.withTransaction {
            cardDao.deleteCardById(cardId)
        }
    }

    override suspend fun getMaxDisplayOrder(): Int = withContext(Dispatchers.IO) {
        cardDao.getMaxDisplayOrder() ?: -1
    }
}
