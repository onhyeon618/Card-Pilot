package com.toyprojects.card_pilot.domain.backup

sealed class BackupException : Exception() {
    class NetworkUnavailable : BackupException()
    class QuotaExceeded : BackupException()
    class NotFound : BackupException()
    class InvalidBackupFile : BackupException()
    class AuthRequired : BackupException()
    class Unknown(override val cause: Throwable) : BackupException()
}
