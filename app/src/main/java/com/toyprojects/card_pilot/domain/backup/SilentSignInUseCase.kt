package com.toyprojects.card_pilot.domain.backup

import com.toyprojects.card_pilot.domain.repository.CloudBackupRepository

class SilentSignInUseCase(
    private val cloudBackupRepository: CloudBackupRepository
) {
    suspend operator fun invoke(): Boolean {
        return cloudBackupRepository.silentSignIn()
    }
}
