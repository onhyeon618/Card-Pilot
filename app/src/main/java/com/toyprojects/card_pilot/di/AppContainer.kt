package com.toyprojects.card_pilot.di

import android.content.Context
import com.toyprojects.card_pilot.data.local.AppDatabase
import com.toyprojects.card_pilot.data.repository.BenefitRepositoryImpl
import com.toyprojects.card_pilot.data.repository.CardRepositoryImpl
import com.toyprojects.card_pilot.data.repository.SettingsRepositoryImpl
import com.toyprojects.card_pilot.data.repository.TransactionRepositoryImpl
import com.toyprojects.card_pilot.domain.repository.BenefitRepository
import com.toyprojects.card_pilot.domain.repository.CardRepository
import com.toyprojects.card_pilot.domain.repository.SettingsRepository
import com.toyprojects.card_pilot.domain.repository.TransactionRepository
import com.toyprojects.card_pilot.domain.usecase.ClearAllDataUseCase
import com.toyprojects.card_pilot.ui.feature.settings.provider.DeviceAppProvider
import com.toyprojects.card_pilot.ui.feature.settings.provider.DeviceAppProviderImpl

/**
 * AppContainer provides manual Dependency Injection.
 */
interface AppContainer {
    val cardRepository: CardRepository
    val benefitRepository: BenefitRepository
    val transactionRepository: TransactionRepository
    val settingsRepository: SettingsRepository
    val deviceAppProvider: DeviceAppProvider
    val clearAllDataUseCase: ClearAllDataUseCase
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    private val database by lazy { AppDatabase.getDatabase(context) }

    override val cardRepository: CardRepository by lazy {
        CardRepositoryImpl(database.cardDao(), database.benefitDao(), database)
    }

    override val benefitRepository: BenefitRepository by lazy {
        BenefitRepositoryImpl(database.benefitDao())
    }

    override val transactionRepository: TransactionRepository by lazy {
        TransactionRepositoryImpl(database.transactionDao())
    }

    override val settingsRepository: SettingsRepository by lazy {
        SettingsRepositoryImpl(context)
    }

    override val deviceAppProvider: DeviceAppProvider by lazy {
        DeviceAppProviderImpl(context)
    }

    override val clearAllDataUseCase: ClearAllDataUseCase by lazy {
        ClearAllDataUseCase(
            cardRepository,
            benefitRepository,
            transactionRepository,
            settingsRepository
        )
    }
}
