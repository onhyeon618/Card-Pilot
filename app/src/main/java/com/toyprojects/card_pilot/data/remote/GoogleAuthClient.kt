package com.toyprojects.card_pilot.data.remote

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.services.drive.DriveScopes
import kotlinx.coroutines.tasks.await

class GoogleAuthClient(context: Context) {

    private val applicationContext = context.applicationContext

    private val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestScopes(Scope(DriveScopes.DRIVE_APPDATA))
        .build()

    private val signInClient: GoogleSignInClient by lazy {
        GoogleSignIn.getClient(applicationContext, signInOptions)
    }

    fun getSignedInAccount(): GoogleSignInAccount? {
        val account = GoogleSignIn.getLastSignedInAccount(applicationContext)
        if (account != null && GoogleSignIn.hasPermissions(account, Scope(DriveScopes.DRIVE_APPDATA))) {
            return account
        }
        return null
    }

    suspend fun silentSignIn(): GoogleSignInAccount? {
        return try {
            val account = signInClient.silentSignIn().await()
            if (GoogleSignIn.hasPermissions(account, Scope(DriveScopes.DRIVE_APPDATA))) {
                account
            } else {
                null
            }
        } catch (e: Exception) {
            // TODO: Firebase Crashlytics 적용
            android.util.Log.e("GoogleAuthClient", "silentSignIn failed", e)
            null
        }
    }

    suspend fun signOut() {
        try {
            signInClient.signOut().await()
        } catch (e: Exception) {
            // TODO: Firebase Crashlytics 적용
            android.util.Log.e("GoogleAuthClient", "signOut failed", e)
        }
    }
}
