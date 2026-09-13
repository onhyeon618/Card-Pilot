package com.toyprojects.card_pilot.domain.auth

class CloudSignOutUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() {
        authRepository.signOut()
    }
}
