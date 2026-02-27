package com.toyprojects.card_pilot.ui.feature.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import com.toyprojects.card_pilot.ui.feature.card.components.CardListItem
import com.toyprojects.card_pilot.ui.shared.CardPilotRipple
import com.toyprojects.card_pilot.ui.shared.GlassScaffold
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardListRoute(
    viewModel: CardListViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onCardClick: (Long) -> Unit = {},
    onAddCard: () -> Unit = {},
    onBack: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CardListScreen(
        uiState = uiState,
        onBack = onBack,
        onCardClick = onCardClick,
        onAddCard = onAddCard
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardListScreen(
    uiState: CardListUiState,
    onCardClick: (Long) -> Unit = {},
    onAddCard: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val cards = uiState.cards

    GlassScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "내 카드 목록",
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
                    navigationIconContentColor = CardPilotColors.TextPrimary,
                    titleContentColor = CardPilotColors.TextPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddCard,
                containerColor = CardPilotColors.SoftSlateIndigo,
                contentColor = CardPilotColors.White,
                shape = CircleShape,
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "새 카드 추가하기"
                )
            }
        }
    ) { paddingValues ->
        if (cards.isEmpty()) {
            /// 카드가 비어있는 경우
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "등록된 카드가 없습니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = CardPilotColors.Secondary
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(
                    top = paddingValues.calculateTopPadding(),
                    bottom = 80.dp + paddingValues.calculateBottomPadding()
                )
            ) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }

                /// 카드 목록
                itemsIndexed(cards) { _, card ->
                    CardListItem(
                        card = card,
                        onClick = { onCardClick(card.id) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))

                    /// 카드 개수
                    Text(
                        text = "총 ${cards.size}장",
                        style = MaterialTheme.typography.bodySmall,
                        color = CardPilotColors.Secondary,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Preview
@Composable
fun CardListScreenPreview() {
    CardPilotTheme {
        CardListScreen(
            uiState = CardListUiState()
        )
    }
}
