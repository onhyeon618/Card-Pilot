package com.toyprojects.card_pilot.domain.backup

import androidx.room.withTransaction
import com.toyprojects.card_pilot.data.local.AppDatabase
import com.toyprojects.card_pilot.data.local.dao.BenefitDao
import com.toyprojects.card_pilot.data.local.dao.CardDao
import com.toyprojects.card_pilot.data.local.dao.TransactionDao
import com.toyprojects.card_pilot.data.local.entity.BenefitEntity
import com.toyprojects.card_pilot.data.local.entity.CardInfoEntity
import com.toyprojects.card_pilot.data.local.entity.TransactionEntity
import com.toyprojects.card_pilot.domain.repository.SettingsRepository
import com.toyprojects.card_pilot.model.ThemeType
import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

class MergeBackupUseCase(
    private val db: AppDatabase,
    private val cardDao: CardDao,
    private val benefitDao: BenefitDao,
    private val transactionDao: TransactionDao,
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(jsonContent: String) {
        val backupData = try {
            Json.decodeFromString<BackupData>(jsonContent)
        } catch (e: Exception) {
            throw IllegalArgumentException("올바르지 않은 백업 파일 형식입니다.", e)
        }

        if (backupData.version > AppDatabase.SCHEMA_VERSION) {
            throw IllegalArgumentException("백업 파일이 더 최신 버전의 앱에서 만들어졌습니다. 앱을 업데이트해주세요.")
        }

        db.withTransaction {
            // 설정 복원
            backupData.settings?.let { settings ->
                settings.theme?.let {
                    try {
                        settingsRepository.setTheme(ThemeType.valueOf(it))
                    } catch (_: Exception) {
                    }
                }
                settingsRepository.setNotiReceiveEnabled(settings.notiReceiveEnabled)
                settingsRepository.setKeepSelectedCard(settings.keepSelectedCard)
            }

            // 데이터 복원 및 기기 내 데이터와 병합
            for (cardBackup in backupData.cards) {
                val existingCard = cardDao.getCardByName(cardBackup.name)
                val cardId = if (existingCard != null) {
                    existingCard.id
                } else {
                    val newCard = CardInfoEntity(
                        name = cardBackup.name,
                        image = cardBackup.image,
                        displayOrder = cardBackup.displayOrder
                    )
                    cardDao.insertCard(newCard)
                }

                for (benefitBackup in cardBackup.benefits) {
                    val existingBenefit = benefitDao.getBenefitByName(cardId, benefitBackup.name)
                    val benefitId = if (existingBenefit != null) {
                        existingBenefit.id
                    } else {
                        val newBenefit = BenefitEntity(
                            cardId = cardId,
                            name = benefitBackup.name,
                            explanation = benefitBackup.explanation,
                            capAmount = benefitBackup.capAmount,
                            dailyLimit = benefitBackup.dailyLimit,
                            oneTimeLimit = benefitBackup.oneTimeLimit,
                            rate = benefitBackup.rate,
                            displayOrder = benefitBackup.displayOrder
                        )
                        benefitDao.insertBenefits(listOf(newBenefit))
                        benefitDao.getBenefitByName(cardId, benefitBackup.name)?.id ?: continue
                    }

                    for (txBackup in benefitBackup.transactions) {
                        val parsedDate = try {
                            LocalDateTime.parse(txBackup.dateTime)
                        } catch (_: DateTimeParseException) {
                            continue
                        }

                        val existingTx = transactionDao.getTransactionByDetails(
                            benefitId = benefitId,
                            merchant = txBackup.merchant,
                            dateTime = parsedDate,
                            amount = txBackup.amount
                        )

                        if (existingTx == null) {
                            transactionDao.insertTransaction(
                                TransactionEntity(
                                    benefitId = benefitId,
                                    merchant = txBackup.merchant,
                                    dateTime = parsedDate,
                                    amount = txBackup.amount,
                                    appliedAmount = txBackup.appliedAmount
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
