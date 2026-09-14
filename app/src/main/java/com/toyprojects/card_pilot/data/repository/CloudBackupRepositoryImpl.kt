package com.toyprojects.card_pilot.data.repository

import com.toyprojects.card_pilot.data.remote.GoogleAuthClient
import com.toyprojects.card_pilot.data.remote.GoogleDriveClient
import com.toyprojects.card_pilot.domain.backup.BackupProgressState
import com.toyprojects.card_pilot.domain.repository.CloudBackupRepository
import kotlinx.coroutines.flow.Flow
import java.io.File

class CloudBackupRepositoryImpl(
    private val authClient: GoogleAuthClient,
    private val driveClient: GoogleDriveClient
) : CloudBackupRepository {

    override fun uploadBackup(zipFile: File): Flow<BackupProgressState> {
        val account = authClient.getSignedInAccount()
            ?: throw IllegalStateException("Google account not signed in")
        return driveClient.uploadBackup(account, zipFile)
    }

    override fun downloadBackup(destFile: File): Flow<BackupProgressState> {
        val account = authClient.getSignedInAccount()
            ?: throw IllegalStateException("Google account not signed in")
        return driveClient.downloadBackup(account, destFile)
    }

    override suspend fun silentSignIn(): Boolean {
        return authClient.silentSignIn() != null
    }

    override fun getSignedInUserEmail(): String? {
        return authClient.getSignedInAccount()?.email
    }

    override suspend fun signOut() {
        authClient.signOut()
    }
}
