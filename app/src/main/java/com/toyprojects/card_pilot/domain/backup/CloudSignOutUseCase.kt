package com.toyprojects.card_pilot.domain.backup

import com.toyprojects.card_pilot.domain.repository.CloudBackupRepository

class CloudSignOutUseCase(
    private val cloudBackupRepository: CloudBackupRepository
) {
    suspend operator fun invoke() {
        cloudBackupRepository.signOut()
    }
}
