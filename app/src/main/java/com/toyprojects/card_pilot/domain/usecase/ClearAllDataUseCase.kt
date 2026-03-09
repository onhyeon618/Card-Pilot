package com.toyprojects.card_pilot.domain.usecase

import com.toyprojects.card_pilot.domain.repository.BenefitRepository
import com.toyprojects.card_pilot.domain.repository.CardRepository
import com.toyprojects.card_pilot.domain.repository.NotificationRepository
import com.toyprojects.card_pilot.domain.repository.SettingsRepository
import com.toyprojects.card_pilot.domain.repository.TransactionRepository

class ClearAllDataUseCase(
    private val cardRepository: CardRepository,
    private val benefitRepository: BenefitRepository,
    private val transactionRepository: TransactionRepository,
    private val notificationRepository: NotificationRepository,
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke() {
        transactionRepository.deleteAllTransactions()
        benefitRepository.deleteAllBenefits()
        cardRepository.deleteAllCards()
        notificationRepository.deleteAllNotifications()
        settingsRepository.clearPreferences()
    }
}
