package com.toyprojects.card_pilot.domain.auth

data class AuthUseCases(
    val silentSignInUseCase: SilentSignInUseCase,
    val signedInUserEmailUseCase: SignedInUserEmailUseCase,
    val cloudSignOutUseCase: CloudSignOutUseCase
)
