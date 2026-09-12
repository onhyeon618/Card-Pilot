package com.toyprojects.card_pilot.data.remote

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.http.FileContent
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.toyprojects.card_pilot.domain.backup.BackupException
import com.toyprojects.card_pilot.domain.backup.BackupProgressState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import com.google.api.services.drive.model.File as DriveFile

class GoogleDriveClient(context: Context) {

    private val applicationContext = context.applicationContext

    companion object {
        private const val BACKUP_FILE_NAME = "cardpilot_backup.zip"
        private const val APP_DATA_FOLDER = "appDataFolder"
    }

    private fun getDriveService(account: GoogleSignInAccount): Drive {
        val credential = GoogleAccountCredential.usingOAuth2(
            applicationContext,
            listOf(DriveScopes.DRIVE_APPDATA)
        ).apply {
            selectedAccount = account.account
        }

        return Drive.Builder(
            NetHttpTransport(),
            GsonFactory.getDefaultInstance(),
            credential
        )
            .setApplicationName("CardPilot")
            .build()
    }

    // 예외 처리를 위한 래핑 함수
    private inline fun <T> runDriveApiCall(block: () -> T): T {
        return try {
            block()
        } catch (e: GoogleJsonResponseException) {
            if (e.details?.errors?.any { it.reason == "storageQuotaExceeded" } == true) {
                throw BackupException.QuotaExceeded()
            }
            throw BackupException.Unknown(e)
        } catch (_: UserRecoverableAuthIOException) {
            throw BackupException.AuthRequired()
        } catch (e: FileNotFoundException) {
            throw BackupException.Unknown(e)
        } catch (_: IOException) {
            throw BackupException.NetworkUnavailable()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            throw BackupException.Unknown(e)
        }
    }

    fun uploadBackup(account: GoogleSignInAccount, zipFile: File): Flow<BackupProgressState> = flow {
        emit(BackupProgressState.PREPARING_NETWORK)
        val driveService = getDriveService(account)

        // 기존 백업 파일이 있는지 확인
        emit(BackupProgressState.CHECKING_EXISTING)
        val existingFileId = runDriveApiCall { findBackupFileId(driveService) }

        if (existingFileId != null) {
            // 있을 경우 새 데이터로 덮어쓰기
            emit(BackupProgressState.UPLOADING_OVERWRITE)
            runDriveApiCall { updateExistingBackupFile(driveService, existingFileId, zipFile) }
        } else {
            // 없을 경우 새 파일 생성
            emit(BackupProgressState.UPLOADING_NEW)
            runDriveApiCall { createNewBackupFile(driveService, zipFile) }
        }
    }.flowOn(Dispatchers.IO)

    fun downloadBackup(account: GoogleSignInAccount, destFile: File): Flow<BackupProgressState> = flow {
        // 구글 드라이브 서비스에 연결
        emit(BackupProgressState.PREPARING_NETWORK)
        val driveService = getDriveService(account)

        // 백업 파일 찾기
        emit(BackupProgressState.CHECKING_EXISTING)
        val fileId = runDriveApiCall { findBackupFileId(driveService) } ?: throw BackupException.NotFound()

        // 백업 파일 다운로드
        emit(BackupProgressState.DOWNLOADING)
        runDriveApiCall {
            FileOutputStream(destFile).use { outputStream ->
                driveService.files().get(fileId).executeMediaAndDownloadTo(outputStream)
            }
        }
    }.flowOn(Dispatchers.IO)

    private fun createNewBackupFile(driveService: Drive, zipFile: File) {
        val fileMetadata = DriveFile().apply {
            name = BACKUP_FILE_NAME
            mimeType = "application/zip"
            parents = listOf(APP_DATA_FOLDER)
        }
        val mediaContent = FileContent("application/zip", zipFile)
        driveService.files().create(fileMetadata, mediaContent).execute()
    }

    private fun updateExistingBackupFile(driveService: Drive, fileId: String, zipFile: File) {
        val fileMetadata = DriveFile().apply {
            name = BACKUP_FILE_NAME
            mimeType = "application/zip"
        }
        val mediaContent = FileContent("application/zip", zipFile)
        driveService.files().update(fileId, fileMetadata, mediaContent).execute()
    }

    private fun findBackupFileId(driveService: Drive): String? {
        val result = driveService.files().list()
            .setSpaces(APP_DATA_FOLDER)
            .setQ("name = '$BACKUP_FILE_NAME'")
            .setFields("files(id, name)")
            .execute()

        val files = result.files
        return if (files != null && files.isNotEmpty()) {
            files[0].id
        } else {
            null
        }
    }
}
