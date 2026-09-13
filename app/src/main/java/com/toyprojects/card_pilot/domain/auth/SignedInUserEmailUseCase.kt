package com.toyprojects.card_pilot.domain.auth

class SignedInUserEmailUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): String? {
        return authRepository.getSignedInUserEmail()
    }
}
