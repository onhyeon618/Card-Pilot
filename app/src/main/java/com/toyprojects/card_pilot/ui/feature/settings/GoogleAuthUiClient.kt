package com.toyprojects.card_pilot.ui.feature.settings

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.api.services.drive.DriveScopes

sealed class SignInResult {
    data class Success(val email: String) : SignInResult()
    object Cancelled : SignInResult()
    object Error : SignInResult()
}

/// UI 계층 - 화면 전환 및 안드로이드 Intent 처리 담당
class GoogleAuthUiClient(private val context: Context) {

    private val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestScopes(Scope(DriveScopes.DRIVE_APPDATA))
        .build()

    private val signInClient = GoogleSignIn.getClient(context, signInOptions)

    // 구글 로그인 창
    fun getSignInIntent(): Intent = signInClient.signInIntent

    // 로그인 요청 결과 처리
    fun handleSignInIntent(intent: Intent?): SignInResult {
        return try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
            val account = task.getResult(Exception::class.java)
            val email = account.email
            if (email != null && GoogleSignIn.hasPermissions(account, Scope(DriveScopes.DRIVE_APPDATA))) {
                SignInResult.Success(email)
            } else {
                SignInResult.Error
            }
        } catch (e: ApiException) {
            if (e.statusCode == GoogleSignInStatusCodes.SIGN_IN_CANCELLED) {
                SignInResult.Cancelled
            } else {
                // TODO: Firebase Crashlytics 적용
                Log.e("GoogleAuthUiClient", "Google Sign-In failed with code: ${e.statusCode}", e)
                SignInResult.Error
            }
        } catch (e: Exception) {
            // TODO: Firebase Crashlytics 적용
            Log.e("GoogleAuthUiClient", "handleSignInIntent failed", e)
            SignInResult.Error
        }
    }
}
