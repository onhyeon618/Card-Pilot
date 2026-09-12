package com.toyprojects.card_pilot.domain.backup

import com.toyprojects.card_pilot.domain.repository.BackupRepository
import com.toyprojects.card_pilot.domain.repository.CloudBackupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import java.io.File

class CloudRestoreUseCase(
    private val backupRepository: BackupRepository,
    private val cloudBackupRepository: CloudBackupRepository
) {
    operator fun invoke(): Flow<BackupProgressState> = flow {
        val tempZip = File.createTempFile("restore", ".zip")
        try {
            emitAll(cloudBackupRepository.downloadBackup(tempZip))
            emit(BackupProgressState.RESTORING_DATA)
            backupRepository.restoreBackupZip(tempZip)
        } finally {
            tempZip.delete()
        }
        emit(BackupProgressState.DONE)
    }
}
