package com.toyprojects.card_pilot.data.repository

import android.content.Context
import androidx.sqlite.db.SimpleSQLiteQuery
import com.toyprojects.card_pilot.data.local.AppDatabase
import com.toyprojects.card_pilot.domain.backup.BackupException
import com.toyprojects.card_pilot.domain.repository.BackupRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

class BackupRepositoryImpl(
    private val context: Context,
    private val db: AppDatabase
) : BackupRepository {

    companion object {
        private const val BACKUP_ZIP_NAME = "backup.zip"
        private const val RESTORE_TEMP_DIR = "restore_temp"
        private const val SQLITE_HEADER = "SQLite format 3\u0000"
        private const val SQLITE_HEADER_SIZE = 16
    }

    override suspend fun createBackupZip(): File = withContext(Dispatchers.IO) {
        // WAL 데이터를 메인 DB로 병합
        db.query(SimpleSQLiteQuery("PRAGMA wal_checkpoint(TRUNCATE)")).close()

        val zipFile = File(context.cacheDir, BACKUP_ZIP_NAME)

        // 파일 복사 중 다른 스레드의 쓰기를 방지하기 위해 트랜잭션 락 획득
        db.runInTransaction {
            val dbName = AppDatabase.DATABASE_NAME
            val dbFile = context.getDatabasePath(dbName)
            val walFile = context.getDatabasePath("$dbName-wal")
            val shmFile = context.getDatabasePath("$dbName-shm")

            ZipOutputStream(FileOutputStream(zipFile)).use { zos ->
                listOf(dbFile, walFile, shmFile).forEach { file ->
                    if (file.exists()) {
                        zos.putNextEntry(ZipEntry(file.name))
                        FileInputStream(file).use { fis -> fis.copyTo(zos) }
                        zos.closeEntry()
                    }
                }
            }
        }

        zipFile
    }

    override suspend fun restoreBackupZip(zipFile: File) = withContext(Dispatchers.IO) {
        db.close()

        val tempDir = unzipToTempDir(zipFile)

        try {
            // SQLite 파일이 맞는지 검증
            validateDatabaseFile(tempDir)

            // 기존 DB 파일을 교체
            replaceDatabaseFiles(tempDir)
        } finally {
            tempDir.deleteRecursively()
        }
    }

    private fun unzipToTempDir(zipFile: File): File {
        val tempDir = File(context.cacheDir, RESTORE_TEMP_DIR).apply {
            deleteRecursively()
            mkdirs()
        }

        ZipInputStream(FileInputStream(zipFile)).use { zis ->
            var entry = zis.nextEntry
            while (entry != null) {
                val destFile = File(tempDir, entry.name)
                destFile.parentFile?.mkdirs()
                FileOutputStream(destFile).use { fos ->
                    zis.copyTo(fos)
                }
                zis.closeEntry()
                entry = zis.nextEntry
            }
        }
        return tempDir
    }

    private fun validateDatabaseFile(tempDir: File) {
        val dbName = AppDatabase.DATABASE_NAME
        val dbFile = File(tempDir, dbName)

        // SQLite DB 파일의 고유 헤더 확인
        if (!dbFile.exists() || dbFile.length() < SQLITE_HEADER_SIZE) {
            throw BackupException.InvalidBackupFile()
        }

        FileInputStream(dbFile).use { stream ->
            val header = ByteArray(SQLITE_HEADER_SIZE)
            val bytesRead = stream.read(header)
            if (bytesRead < SQLITE_HEADER_SIZE) {
                throw BackupException.InvalidBackupFile()
            }
            val expectedHeader = SQLITE_HEADER.toByteArray(Charsets.UTF_8)
            if (!header.contentEquals(expectedHeader)) {
                throw BackupException.InvalidBackupFile()
            }
        }
    }

    private fun replaceDatabaseFiles(tempDir: File) {
        val dbName = AppDatabase.DATABASE_NAME

        val dbFile = context.getDatabasePath(dbName)
        val shmFile = context.getDatabasePath("$dbName-shm")
        val walFile = context.getDatabasePath("$dbName-wal")

        // 오류 발생 시 롤백하기 위해 기존 DB 임시 백업
        val backupDbFile = File(dbFile.parentFile, "$dbName.bak")
        val backupShmFile = File(shmFile.parentFile, "$dbName-shm.bak")
        val backupWalFile = File(walFile.parentFile, "$dbName-wal.bak")

        try {
            // 기존 DB 임시 백업 (실패 시 복원 용도)
            if (dbFile.exists()) dbFile.copyTo(backupDbFile, overwrite = true)
            if (shmFile.exists()) shmFile.copyTo(backupShmFile, overwrite = true)
            if (walFile.exists()) walFile.copyTo(backupWalFile, overwrite = true)

            // 기존 DB 삭제
            dbFile.delete()
            shmFile.delete()
            walFile.delete()

            // 다운받은 DB로 교체
            tempDir.listFiles()?.forEach { tempFile ->
                val destFile = context.getDatabasePath(tempFile.name)
                destFile.parentFile?.mkdirs()
                tempFile.copyTo(destFile, overwrite = true)
            }
        } catch (e: Exception) {
            // 실패 시 롤백
            if (backupDbFile.exists()) backupDbFile.copyTo(dbFile, overwrite = true)
            if (backupShmFile.exists()) backupShmFile.copyTo(shmFile, overwrite = true)
            if (backupWalFile.exists()) backupWalFile.copyTo(walFile, overwrite = true)
            throw e
        } finally {
            // 임시 백업 파일 삭제
            backupDbFile.delete()
            backupShmFile.delete()
            backupWalFile.delete()
        }
    }
}
