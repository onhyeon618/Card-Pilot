package com.toyprojects.card_pilot.domain.repository

import com.toyprojects.card_pilot.domain.backup.BackupProgressState
import kotlinx.coroutines.flow.Flow
import java.io.File

interface CloudBackupRepository {
    fun uploadBackup(zipFile: File): Flow<BackupProgressState>
    fun downloadBackup(destFile: File): Flow<BackupProgressState>
}
