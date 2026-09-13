package com.toyprojects.card_pilot.data.remote

import android.content.Context
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.services.drive.DriveScopes
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

/// Data 계층 - 화면 없이 조용히 일어나는 순수 데이터 통신 전담
class GoogleAuthClient(context: Context) {

    private val applicationContext = context.applicationContext

    private val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestScopes(Scope(DriveScopes.DRIVE_APPDATA))
        .build()

    private val signInClient: GoogleSignInClient by lazy {
        GoogleSignIn.getClient(applicationContext, signInOptions)
    }

    private fun hasRequiredPermissions(account: GoogleSignInAccount?): Boolean {
        return account != null && GoogleSignIn.hasPermissions(account, Scope(DriveScopes.DRIVE_APPDATA))
    }

    fun getSignedInAccount(): GoogleSignInAccount? {
        val account = GoogleSignIn.getLastSignedInAccount(applicationContext)
        return if (hasRequiredPermissions(account)) account else null
    }

    // 이전 로그인 기록 복구
    suspend fun silentSignIn(): GoogleSignInAccount? {
        return try {
            val account = signInClient.silentSignIn().await()
            if (hasRequiredPermissions(account)) account else null
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            // TODO: Firebase Crashlytics 적용
            Log.e("GoogleAuthClient", "silentSignIn failed", e)
            null
        }
    }

    // 로그아웃
    suspend fun signOut() {
        try {
            signInClient.signOut().await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            // TODO: Firebase Crashlytics 적용
            Log.e("GoogleAuthClient", "signOut failed", e)
        }
    }
}
