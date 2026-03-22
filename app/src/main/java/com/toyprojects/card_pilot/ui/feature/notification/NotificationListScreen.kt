package com.toyprojects.card_pilot.ui.feature.notification

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.toyprojects.card_pilot.ui.AppViewModelProvider
import com.toyprojects.card_pilot.ui.feature.notification.components.NotificationListItem
import com.toyprojects.card_pilot.ui.shared.CardPilotRipple
import com.toyprojects.card_pilot.ui.shared.GlassScaffold
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme

@Composable
fun NotificationListRoute(
    onItemClick: () -> Unit,
    onBack: () -> Unit,
    viewModel: NotificationListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NotificationListScreen(
        uiState = uiState,
        onItemClick = onItemClick,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationListScreen(
    uiState: NotificationListUiState,
    onItemClick: () -> Unit,
    onBack: () -> Unit
) {
    GlassScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "지출 알림 목록",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    CardPilotRipple {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "뒤로"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = CardPilotColors.textPrimary,
                    titleContentColor = CardPilotColors.textPrimary
                )
            )
        }
    ) { paddingValues ->
        if (uiState.notifications.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "수신한 알림이 없습니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = CardPilotColors.secondary
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = paddingValues.calculateTopPadding()
                    ),
                contentPadding = PaddingValues(
                    bottom = paddingValues.calculateBottomPadding() + 16.dp
                )
            ) {
                items(
                    count = uiState.notifications.size,
                    key = { index -> uiState.notifications[index].id }
                ) { index ->
                    val item = uiState.notifications[index]
                    NotificationListItem(
                        appName = item.appName,
                        timestamp = item.timestamp,
                        amount = item.amount,
                        content = item.content,
                        onClick = onItemClick
                    )
                    if (index < uiState.notifications.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 24.dp),
                            color = CardPilotColors.gray200,
                            thickness = 0.5.dp
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun NotificationListScreenPreview() {
    CardPilotTheme {
        NotificationListScreen(
            uiState = NotificationListUiState(),
            onItemClick = {},
            onBack = {}
        )
    }
}
