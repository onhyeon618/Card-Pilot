package com.toyprojects.card_pilot.domain.backup

import com.toyprojects.card_pilot.domain.repository.CloudBackupRepository

class SignedInUserEmailUseCase(
    private val cloudBackupRepository: CloudBackupRepository
) {
    operator fun invoke(): String? {
        return cloudBackupRepository.getSignedInUserEmail()
    }
}
