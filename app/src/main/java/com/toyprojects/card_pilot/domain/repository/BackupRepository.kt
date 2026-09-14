package com.toyprojects.card_pilot.domain.repository

import java.io.File

interface BackupRepository {
    suspend fun createBackupZip(): File
    suspend fun restoreBackupZip(zipFile: File)
}
