package com.toyprojects.card_pilot.ui.feature.settings

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.services.drive.DriveScopes

class GoogleAuthUiClient(private val context: Context) {

    private val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestScopes(Scope(DriveScopes.DRIVE_APPDATA))
        .build()

    private val signInClient = GoogleSignIn.getClient(context, signInOptions)

    fun getSignInIntent(): Intent = signInClient.signInIntent

    fun getSignedInAccountEmail(intent: Intent?): String? {
        return try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
            val account = task.getResult(Exception::class.java)
            if (GoogleSignIn.hasPermissions(account, Scope(DriveScopes.DRIVE_APPDATA))) {
                account.email
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }
}
