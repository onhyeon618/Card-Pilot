package com.toyprojects.card_pilot.domain.backup

import com.toyprojects.card_pilot.data.local.dao.BenefitDao
import com.toyprojects.card_pilot.data.local.dao.CardDao
import com.toyprojects.card_pilot.data.local.dao.TransactionDao
import com.toyprojects.card_pilot.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ExportBackupUseCase(
    private val cardDao: CardDao,
    private val benefitDao: BenefitDao,
    private val transactionDao: TransactionDao,
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(): String {
        val cardsEntity = cardDao.getAllCardsSync()

        val cardsBackup = cardsEntity.map { card ->
            val benefitsEntity = benefitDao.getBenefitsOfCardSync(card.id)
            val benefitsBackup = benefitsEntity.map { benefit ->
                val transactionsEntity = transactionDao.getTransactionsForBenefitSync(benefit.id)
                val transactionsBackup = transactionsEntity.map { transaction ->
                    TransactionBackup(
                        id = transaction.id,
                        merchant = transaction.merchant,
                        dateTime = transaction.dateTime.toString(),
                        amount = transaction.amount,
                        appliedAmount = transaction.appliedAmount
                    )
                }
                BenefitBackup(
                    id = benefit.id,
                    name = benefit.name,
                    explanation = benefit.explanation,
                    capAmount = benefit.capAmount,
                    dailyLimit = benefit.dailyLimit,
                    oneTimeLimit = benefit.oneTimeLimit,
                    rate = benefit.rate,
                    displayOrder = benefit.displayOrder,
                    transactions = transactionsBackup
                )
            }
            CardBackup(
                id = card.id,
                name = card.name,
                image = card.image,
                displayOrder = card.displayOrder,
                benefits = benefitsBackup
            )
        }

        val settingsBackup = SettingsBackup(
            theme = settingsRepository.themeType.first().name,
            notiReceiveEnabled = settingsRepository.notiReceiveEnabled.first(),
            keepSelectedCard = settingsRepository.keepSelectedCard.first()
        )

        val backupData = BackupData(
            version = com.toyprojects.card_pilot.data.local.AppDatabase.SCHEMA_VERSION,
            cards = cardsBackup,
            settings = settingsBackup
        )

        return Json.encodeToString(backupData)
    }
}