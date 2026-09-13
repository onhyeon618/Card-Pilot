package com.toyprojects.card_pilot.domain.backup

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.ByteArrayContent
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.IOException

class GoogleDriveClient(private val context: Context) {

    private val backupFileName = "cardpilot_backup.json"
    private val appDataFolder = "appDataFolder"

    private fun getDriveService(account: GoogleSignInAccount): Drive {
        val credential = GoogleAccountCredential.usingOAuth2(
            context,
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

    suspend fun uploadBackup(
        account: GoogleSignInAccount,
        jsonContent: String,
        onProgress: (String) -> Unit = {}
    ): File? = withContext(Dispatchers.IO) {
        onProgress("구글 드라이브와 통신을 준비하는 중...")
        val driveService = getDriveService(account)

        // 기존 백업 파일이 있는지 확인
        onProgress("기존 백업 파일을 확인하는 중...")
        val existingFileId = findBackupFileId(driveService)

        val fileMetadata = File().apply {
            name = backupFileName
            mimeType = "application/json"
            if (existingFileId == null) {
                parents = listOf(appDataFolder)
            }
        }

        val mediaContent = ByteArrayContent.fromString("application/json", jsonContent)

        try {
            if (existingFileId != null) {
                // 있을 경우 새 데이터로 덮어쓰기
                onProgress("안전하게 기존 데이터를 덮어쓰는 중...")
                driveService.files().update(existingFileId, fileMetadata, mediaContent).execute()
            } else {
                // 없을 경우 새 파일 생성
                onProgress("새로운 백업 파일을 클라우드에 생성하는 중...")
                driveService.files().create(fileMetadata, mediaContent).execute()
            }
        } catch (e: Exception) {
            throw IOException("Failed to upload backup to Google Drive", e)
        }
    }

    suspend fun downloadBackup(account: GoogleSignInAccount, onProgress: (String) -> Unit = {}): String? =
        withContext(Dispatchers.IO) {
            onProgress("구글 드라이브와 통신을 준비하는 중...")
            val driveService = getDriveService(account)

            onProgress("클라우드에서 백업 파일을 찾는 중...")
            val fileId = findBackupFileId(driveService) ?: return@withContext null

            try {
                onProgress("백업된 데이터를 기기로 다운로드하는 중...")
                val outputStream = ByteArrayOutputStream()
                driveService.files().get(fileId).executeMediaAndDownloadTo(outputStream)
                return@withContext outputStream.toString("UTF-8")
            } catch (e: Exception) {
                throw IOException("Failed to download backup from Google Drive", e)
            }
        }

    private fun findBackupFileId(driveService: Drive): String? {
        val result = driveService.files().list()
            .setSpaces(appDataFolder)
            .setQ("name = '$backupFileName'")
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