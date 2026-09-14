package com.toyprojects.card_pilot.domain.backup

import com.toyprojects.card_pilot.domain.repository.BackupRepository
import com.toyprojects.card_pilot.domain.repository.CloudBackupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class CloudBackupUseCase(
    private val backupRepository: BackupRepository,
    private val cloudBackupRepository: CloudBackupRepository
) {
    operator fun invoke(): Flow<BackupProgressState> = flow {
        emit(BackupProgressState.EXTRACTING_DATA)
        val zipFile = backupRepository.createBackupZip()
        try {
            emitAll(cloudBackupRepository.uploadBackup(zipFile))
        } finally {
            zipFile.delete()
        }
        emit(BackupProgressState.DONE)
    }
}
