package com.toyprojects.card_pilot.ui.feature.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toyprojects.card_pilot.domain.model.NotificationMessage
import com.toyprojects.card_pilot.domain.repository.NotificationRepository
import com.toyprojects.card_pilot.ui.feature.settings.provider.DeviceAppProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.format.DateTimeFormatter

data class NotificationItemUiState(
    val id: Long,
    val appName: String,
    val timestamp: String,
    val amount: String,
    val content: String,
    val originalMessage: NotificationMessage
)

data class NotificationListUiState(
    val isLoading: Boolean = false,
    val notifications: List<NotificationItemUiState> = emptyList()
)

class NotificationListViewModel(
    private val notificationRepository: NotificationRepository,
    private val deviceAppProvider: DeviceAppProvider
) : ViewModel() {
    private val formatter = DateTimeFormatter.ofPattern("MM/dd HH:mm")

    val uiState: StateFlow<NotificationListUiState> = notificationRepository.getAllNotifications()
        .map { messages ->
            NotificationListUiState(
                isLoading = false,
                notifications = messages.toUiStateList()
            )
        }
        .flowOn(Dispatchers.Default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NotificationListUiState(isLoading = true)
        )

    private suspend fun List<NotificationMessage>.toUiStateList(): List<NotificationItemUiState> {
        return this.mapNotNull { message ->
            if (message.amount.isBlank()) return@mapNotNull null
            
            val appName = deviceAppProvider.getAppName(message.packageName)
            val formattedTime = message.timestamp.format(formatter)

            NotificationItemUiState(
                id = message.id,
                appName = appName,
                timestamp = formattedTime,
                amount = message.amount,
                content = message.place.ifBlank { "사용처 알 수 없음" },
                originalMessage = message
            )
        }
    }
}
