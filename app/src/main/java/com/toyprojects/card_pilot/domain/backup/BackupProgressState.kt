package com.toyprojects.card_pilot.domain.backup

enum class BackupProgressState {
    EXTRACTING_DATA,
    PREPARING_NETWORK,
    CHECKING_EXISTING,
    UPLOADING_OVERWRITE,
    UPLOADING_NEW,
    DOWNLOADING,
    RESTORING_DATA,
    DONE
}
