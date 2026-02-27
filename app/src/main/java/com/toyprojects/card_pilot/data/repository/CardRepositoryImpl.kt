package com.toyprojects.card_pilot.data.repository

import com.toyprojects.card_pilot.data.local.AppDatabase
import com.toyprojects.card_pilot.data.local.dao.BenefitDao
import com.toyprojects.card_pilot.data.local.dao.CardDao
import com.toyprojects.card_pilot.domain.repository.CardRepository
import com.toyprojects.card_pilot.mock.MockData
import com.toyprojects.card_pilot.model.CardInfo
import com.toyprojects.card_pilot.model.CardSimpleInfo
import kotlinx.coroutines.flow.Flow

class CardRepositoryImpl(
    private val cardDao: CardDao,
    private val benefitDao: BenefitDao,
    private val database: AppDatabase
) : CardRepository {
    override fun getAllCards(): Flow<List<CardSimpleInfo>> {
        // return cardDao.getAllCards().map { results ->
        //     results.map { result ->
        //         CardSimpleInfo(
        //             id = result.id,
        //             name = result.name,
        //             image = result.image,
        //             displayOrder = result.displayOrder
        //         )
        //     }
        // }
        return kotlinx.coroutines.flow.flowOf(MockData.mockCards)
    }

    override suspend fun getCardById(cardId: Long): CardSimpleInfo? {
//        return cardDao.getCardById(cardId)?.let { result ->
//            CardSimpleInfo(
//                id = result.id,
//                name = result.name,
//                image = result.image,
//                displayOrder = result.displayOrder
//            )
//        }
        return MockData.mockCards[cardId.toInt() - 1]
    }

    override fun getCardWithTotalAmount(
        cardId: Long,
        yearMonth: java.time.YearMonth
    ): Flow<CardInfo?> {
        // val startDateTime = yearMonth.atDay(1).atStartOfDay()
        // val endDateTime = yearMonth.plusMonths(1).atDay(1).atStartOfDay()

        // val cardFlow = cardDao.getCardWithTotalAmount(cardId, startDateTime, endDateTime)
        // val benefitsFlow = benefitDao.getBenefitListOfCard(cardId, startDateTime, endDateTime)

        // return cardFlow.combine(benefitsFlow) { cardResult, benefitsResult ->
        //     cardResult?.let { card ->
        //         CardInfo(
        //             id = card.card.id,
        //             name = card.card.name,
        //             image = card.card.image,
        //             usageAmount = card.totalAmount,
        //             benefits = benefitsResult.map { benefit ->
        //                 Benefit(
        //                     id = benefit.benefit.id,
        //                     name = benefit.benefit.name,
        //                     explanation = benefit.benefit.explanation,
        //                     capAmount = benefit.benefit.capAmount,
        //                     usedAmount = benefit.usedAmount,
        //                     displayOrder = benefit.benefit.displayOrder
        //                 )
        //             },
        //             displayOrder = card.card.displayOrder
        //         )
        //     }
        // }
        return kotlinx.coroutines.flow.flowOf(MockData.mockCardDetails[cardId])
    }

    override suspend fun insertCard(card: CardInfo): Long {
//        return database.withTransaction {
//            val nextDisplayOrder = (cardDao.getMaxDisplayOrder() ?: -1) + 1
//
//            val entity = CardInfoEntity(
//                name = card.name,
//                image = card.image,
//                displayOrder = nextDisplayOrder
//            )
//            val newCardId = cardDao.insertCard(entity)
//
//            // 혜택 데이터도 추가
//            val benefitEntities = card.benefits.map { benefit ->
//                BenefitEntity(
//                    cardId = newCardId,
//                    name = benefit.name,
//                    capAmount = benefit.capAmount,
//                    explanation = benefit.explanation,
//                    displayOrder = benefit.displayOrder
//                )
//            }
//            benefitDao.insertBenefits(benefitEntities)
//            newCardId
//        }
        val nextNum = MockData.mockCards.size
        MockData.mockCards.add(
            CardSimpleInfo(
                id = nextNum.toLong(),
                name = card.name,
                image = "",
                displayOrder = nextNum
            )
        )
        MockData.mockCardDetails[nextNum.toLong()] = card
        return nextNum.toLong()
    }

    override suspend fun updateCard(card: CardInfo) {
//        database.withTransaction {
//            val entity = CardInfoEntity(
//                id = card.id,
//                name = card.name,
//                image = card.image,
//                displayOrder = card.displayOrder
//            )
//            cardDao.updateCard(entity)
//
//            // 기존 혜택 목록
//            val originalBenefits = benefitDao.getBenefitsOfCardSync(card.id)
//
//            // 새 혜택 목록에 없는 기존 혜택 추출
//            val remainingBenefitIds = card.benefits.map { it.id }.toSet()
//            val benefitsToDeleteIds = originalBenefits
//                .filter { it.id !in remainingBenefitIds }
//                .map { it.id }
//
//            // 반드시 삭제 먼저 수행해야 UNIQUE 제약 조건(cardId, name) 충돌 방지 가능
//            if (benefitsToDeleteIds.isNotEmpty()) {
//                benefitDao.deleteBenefitsByIds(benefitsToDeleteIds)
//            }
//
//            val benefitsToInsert = mutableListOf<BenefitEntity>()
//            val benefitsToUpdate = mutableListOf<BenefitEntity>()
//
//            card.benefits.forEach { benefit ->
//                val benefitEntity = BenefitEntity(
//                    id = benefit.id,
//                    cardId = card.id,
//                    name = benefit.name,
//                    capAmount = benefit.capAmount,
//                    explanation = benefit.explanation,
//                    displayOrder = benefit.displayOrder
//                )
//
//                if (benefit.id == 0L) {
//                    // 새로 추가된 혜택
//                    benefitsToInsert.add(benefitEntity)
//                } else {
//                    // 기존 혜택 - 업데이트
//                    benefitsToUpdate.add(benefitEntity)
//                }
//            }
//
//            // 항상 업데이트 먼저 수행
//            if (benefitsToUpdate.isNotEmpty()) {
//                benefitDao.updateBenefits(benefitsToUpdate)
//            }
//            if (benefitsToInsert.isNotEmpty()) {
//                benefitDao.insertBenefits(benefitsToInsert)
//            }
//        }
    }

    override suspend fun updateCardOrders(cards: List<CardSimpleInfo>) {
//        val updates = cards.map { card ->
//            CardOrderUpdate(
//                id = card.id,
//                displayOrder = card.displayOrder
//            )
//        }
//        cardDao.updateCardDisplayOrders(updates)
    }

    override suspend fun deleteCard(cardId: Long) {
//        database.withTransaction {
//            cardDao.deleteCardById(cardId)
//        }
    }

    override suspend fun getMaxDisplayOrder(): Int = cardDao.getMaxDisplayOrder() ?: -1
}
