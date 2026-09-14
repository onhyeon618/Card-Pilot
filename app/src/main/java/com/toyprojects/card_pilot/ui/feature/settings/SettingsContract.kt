package com.toyprojects.card_pilot.ui.feature.settings

import com.toyprojects.card_pilot.model.ThemeType

enum class PendingGoogleAuthAction { BACKUP, RESTORE, NONE }

data class SettingsState(
    val currentTheme: ThemeType = ThemeType.PURPLE,
    val notiReceiveEnabled: Boolean = false,
    val keepSelectedCard: Boolean = false,
    val isUpdateAvailable: Boolean = false,
    val googleAccountEmail: String? = null,
    val isLoading: Boolean = false,
    val loadingMessage: String? = null
)

interface SettingsActions {
    fun onThemeSelected(themeType: ThemeType)
    fun setKeepSelectedCard(keep: Boolean)
    fun onBack()
    fun onCardListClick()
    fun onAddCardClick()
    fun onNotificationSettingsClick()
    fun onResetDataClick()
    fun onRequestGoogleSignIn()
    fun onBackupDataClick()
    fun onRestoreDataClick()
    fun onSignOutClick()
}
