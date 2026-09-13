package com.toyprojects.card_pilot.data.repository

import com.toyprojects.card_pilot.data.remote.GoogleAuthClient
import com.toyprojects.card_pilot.domain.auth.AuthRepository

class AuthRepositoryImpl(
    private val authClient: GoogleAuthClient
) : AuthRepository {
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
