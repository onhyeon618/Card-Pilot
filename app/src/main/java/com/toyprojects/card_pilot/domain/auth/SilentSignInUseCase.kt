package com.toyprojects.card_pilot.domain.auth

class SilentSignInUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Boolean {
        return authRepository.silentSignIn()
    }
}
