package com.toyprojects.card_pilot.domain.auth

interface AuthRepository {
    suspend fun silentSignIn(): Boolean
    fun getSignedInUserEmail(): String?
    suspend fun signOut()
}
