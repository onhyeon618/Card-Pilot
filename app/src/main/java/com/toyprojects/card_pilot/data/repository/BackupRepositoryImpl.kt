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

    override suspend fun createBackupZip(): File = withContext(Dispatchers.IO) {
        // 1. 먼저 WAL 데이터를 메인 DB로 병합 (트랜잭션 밖에서 실행해야 함)
        db.query(SimpleSQLiteQuery("PRAGMA wal_checkpoint(TRUNCATE)")).close()

        val zipFile = File(context.cacheDir, "backup.zip")

        // 2. 파일 복사 중 다른 스레드의 쓰기(Write)를 방지하기 위해 트랜잭션 락(Lock) 획득
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
                
                // Add JPG image files from filesDir
                context.filesDir.listFiles { file -> file.extension.equals("jpg", ignoreCase = true) }?.forEach { imgFile ->
                    zos.putNextEntry(ZipEntry(imgFile.name))
                    FileInputStream(imgFile).use { fis -> fis.copyTo(zos) }
                    zos.closeEntry()
                }
            }
        }

        zipFile
    }

    override suspend fun restoreBackupZip(zipFile: File) = withContext(Dispatchers.IO) {
        db.close()

        val tempDir = unzipToTempDir(zipFile)

        try {
            validateDatabaseFile(tempDir)
            replaceDatabaseFiles(tempDir)
        } finally {
            tempDir.deleteRecursively()
        }
    }

    private fun validateDatabaseFile(tempDir: File) {
        val dbName = AppDatabase.DATABASE_NAME
        val dbFile = File(tempDir, dbName)

        if (!dbFile.exists() || dbFile.length() < 16) {
            throw BackupException.InvalidBackupFile()
        }

        FileInputStream(dbFile).use { fis ->
            val header = ByteArray(16)
            val bytesRead = fis.read(header)
            if (bytesRead < 16) {
                throw BackupException.InvalidBackupFile()
            }
            val expectedHeader = "SQLite format 3\u0000".toByteArray(Charsets.UTF_8)
            if (!header.contentEquals(expectedHeader)) {
                throw BackupException.InvalidBackupFile()
            }
        }
    }

    private fun unzipToTempDir(zipFile: File): File {
        val tempDir = File(context.cacheDir, "restore_temp").apply {
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

    private fun replaceDatabaseFiles(tempDir: File) {
        val dbName = AppDatabase.DATABASE_NAME

        val dbFile = context.getDatabasePath(dbName)
        val shmFile = context.getDatabasePath("$dbName-shm")
        val walFile = context.getDatabasePath("$dbName-wal")

        // 기존 파일 백업
        val backupDbFile = File(dbFile.parentFile, "$dbName.bak")
        val backupShmFile = File(shmFile.parentFile, "$dbName-shm.bak")
        val backupWalFile = File(walFile.parentFile, "$dbName-wal.bak")
        
        val copiedImages = mutableListOf<File>()

        try {
            if (dbFile.exists()) dbFile.copyTo(backupDbFile, overwrite = true)
            if (shmFile.exists()) shmFile.copyTo(backupShmFile, overwrite = true)
            if (walFile.exists()) walFile.copyTo(backupWalFile, overwrite = true)

            // 기존 파일 확실히 삭제
            dbFile.delete()
            shmFile.delete()
            walFile.delete()

            // 다시 디렉토리에서 파일들을 복사
            tempDir.listFiles()?.forEach { tempFile ->
                if (tempFile.extension.equals("jpg", ignoreCase = true)) {
                    // 이미지 파일 복원
                    val destFile = File(context.filesDir, tempFile.name)
                    tempFile.copyTo(destFile, overwrite = true)
                    copiedImages.add(destFile)
                } else {
                    // 데이터베이스 파일 복원
                    val destFile = context.getDatabasePath(tempFile.name)
                    destFile.parentFile?.mkdirs()
                    tempFile.copyTo(destFile, overwrite = true)
                }
            }
        } catch (e: Exception) {
            // 실패 시 롤백
            if (backupDbFile.exists()) backupDbFile.copyTo(dbFile, overwrite = true)
            if (backupShmFile.exists()) backupShmFile.copyTo(shmFile, overwrite = true)
            if (backupWalFile.exists()) backupWalFile.copyTo(walFile, overwrite = true)
            
            // 이미지 롤백 (새로 추가된 파일 삭제)
            copiedImages.forEach { 
                if (it.exists()) it.delete() 
            }
            throw e
        } finally {
            // 백업 파일 정리
            backupDbFile.delete()
            backupShmFile.delete()
            backupWalFile.delete()
        }
    }
}
