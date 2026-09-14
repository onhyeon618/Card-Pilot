package com.toyprojects.card_pilot.di

import android.content.Context
import com.toyprojects.card_pilot.data.local.AppDatabase
import com.toyprojects.card_pilot.data.provider.LocalNotificationProviderImpl
import com.toyprojects.card_pilot.data.remote.GoogleAuthClient
import com.toyprojects.card_pilot.data.remote.GoogleDriveClient
import com.toyprojects.card_pilot.data.repository.BackupRepositoryImpl
import com.toyprojects.card_pilot.data.repository.BenefitRepositoryImpl
import com.toyprojects.card_pilot.data.repository.CardRepositoryImpl
import com.toyprojects.card_pilot.data.repository.CloudBackupRepositoryImpl
import com.toyprojects.card_pilot.data.repository.NotificationRepositoryImpl
import com.toyprojects.card_pilot.data.repository.SettingsRepositoryImpl
import com.toyprojects.card_pilot.data.repository.TransactionRepositoryImpl
import com.toyprojects.card_pilot.domain.backup.BackupUseCases
import com.toyprojects.card_pilot.domain.backup.CloudBackupUseCase
import com.toyprojects.card_pilot.domain.backup.CloudRestoreUseCase
import com.toyprojects.card_pilot.domain.backup.CloudSignOutUseCase
import com.toyprojects.card_pilot.domain.backup.SignedInUserEmailUseCase
import com.toyprojects.card_pilot.domain.backup.SilentSignInUseCase
import com.toyprojects.card_pilot.domain.parser.NotificationParserFactory
import com.toyprojects.card_pilot.domain.provider.LocalNotificationProvider
import com.toyprojects.card_pilot.domain.repository.BackupRepository
import com.toyprojects.card_pilot.domain.repository.BenefitRepository
import com.toyprojects.card_pilot.domain.repository.CardRepository
import com.toyprojects.card_pilot.domain.repository.CloudBackupRepository
import com.toyprojects.card_pilot.domain.repository.NotificationRepository
import com.toyprojects.card_pilot.domain.repository.SettingsRepository
import com.toyprojects.card_pilot.domain.repository.TransactionRepository
import com.toyprojects.card_pilot.domain.usecase.ClearAllDataUseCase
import com.toyprojects.card_pilot.domain.usecase.ProcessNotificationUseCase
import com.toyprojects.card_pilot.domain.usecase.SaveTransactionUseCase
import com.toyprojects.card_pilot.ui.feature.settings.provider.DeviceAppProvider
import com.toyprojects.card_pilot.ui.feature.settings.provider.DeviceAppProviderImpl
import com.toyprojects.card_pilot.ui.feature.settings.provider.NotificationPermissionProvider
import com.toyprojects.card_pilot.ui.feature.settings.provider.NotificationPermissionProviderImpl

/**
 * AppContainer provides manual Dependency Injection.
 */
interface AppContainer {
    val cardRepository: CardRepository
    val benefitRepository: BenefitRepository
    val transactionRepository: TransactionRepository
    val notificationRepository: NotificationRepository
    val settingsRepository: SettingsRepository
    val deviceAppProvider: DeviceAppProvider
    val notificationPermissionProvider: NotificationPermissionProvider
    val processNotificationUseCase: ProcessNotificationUseCase
    val saveTransactionUseCase: SaveTransactionUseCase
    val clearAllDataUseCase: ClearAllDataUseCase
    val notificationParserFactory: NotificationParserFactory
    val localNotificationProvider: LocalNotificationProvider
    val backupRepository: BackupRepository
    val cloudBackupRepository: CloudBackupRepository
    val backupUseCases: BackupUseCases
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

    override val notificationRepository: NotificationRepository by lazy {
        NotificationRepositoryImpl(database.notificationDao())
    }

    override val settingsRepository: SettingsRepository by lazy {
        SettingsRepositoryImpl(context)
    }

    override val deviceAppProvider: DeviceAppProvider by lazy {
        DeviceAppProviderImpl(context)
    }

    override val notificationPermissionProvider: NotificationPermissionProvider by lazy {
        NotificationPermissionProviderImpl(context)
    }

    override val processNotificationUseCase: ProcessNotificationUseCase by lazy {
        ProcessNotificationUseCase(
            notificationRepository,
            notificationParserFactory,
            settingsRepository,
            localNotificationProvider
        )
    }

    override val saveTransactionUseCase: SaveTransactionUseCase by lazy {
        SaveTransactionUseCase(
            transactionRepository,
            notificationRepository
        )
    }

    override val clearAllDataUseCase: ClearAllDataUseCase by lazy {
        ClearAllDataUseCase(
            cardRepository,
            benefitRepository,
            transactionRepository,
            notificationRepository,
            settingsRepository
        )
    }

    override val notificationParserFactory: NotificationParserFactory by lazy {
        NotificationParserFactory()
    }

    override val localNotificationProvider: LocalNotificationProvider by lazy {
        LocalNotificationProviderImpl(context)
    }

    override val backupRepository: BackupRepository by lazy {
        BackupRepositoryImpl(
            context.applicationContext,
            database
        )
    }

    override val cloudBackupRepository: CloudBackupRepository by lazy {
        CloudBackupRepositoryImpl(
            GoogleAuthClient(context.applicationContext),
            GoogleDriveClient(context.applicationContext)
        )
    }

    override val backupUseCases: BackupUseCases by lazy {
        BackupUseCases(
            cloudBackupUseCase = CloudBackupUseCase(backupRepository, cloudBackupRepository),
            cloudRestoreUseCase = CloudRestoreUseCase(backupRepository, cloudBackupRepository),
            silentSignInUseCase = SilentSignInUseCase(cloudBackupRepository),
            signedInUserEmailUseCase = SignedInUserEmailUseCase(cloudBackupRepository),
            cloudSignOutUseCase = CloudSignOutUseCase(cloudBackupRepository)
        )
    }
}
