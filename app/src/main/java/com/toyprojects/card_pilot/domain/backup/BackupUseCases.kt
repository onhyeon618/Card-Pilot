package com.toyprojects.card_pilot.domain.backup

data class BackupUseCases(
    val cloudBackupUseCase: CloudBackupUseCase,
    val cloudRestoreUseCase: CloudRestoreUseCase
)
