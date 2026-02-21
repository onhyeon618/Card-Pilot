package com.toyprojects.card_pilot.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RippleConfiguration
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.toyprojects.card_pilot.model.CardInfo
import com.toyprojects.card_pilot.ui.components.CardListItem
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardListScreen(
    onBack: () -> Unit = {},
    onCardClick: (CardInfo) -> Unit = {},
    onAddCard: () -> Unit = {}
) {
    // Mock Data
    val cards = listOf(
        CardInfo("The Red", ""),
        CardInfo("taptap O", ""),
        CardInfo("Mr.Life", "")
    )

    com.toyprojects.card_pilot.ui.components.GlassScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "내 카드 목록",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    CompositionLocalProvider(
                        LocalRippleConfiguration provides RippleConfiguration(color = CardPilotColors.PastelViolet)
                    ) {
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
            androidx.compose.material3.FloatingActionButton(
                onClick = onAddCard,
                containerColor = CardPilotColors.SoftSlateIndigo,
                contentColor = CardPilotColors.White,
                shape = androidx.compose.foundation.shape.CircleShape,
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "카드 추가"
                )
            }
        }
    ) { paddingValues ->
        if (cards.isEmpty()) {
            /// Empty State
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
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    top = paddingValues.calculateTopPadding(),
                    bottom = 80.dp + paddingValues.calculateBottomPadding()
                )
            ) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }

                /// Card List
                itemsIndexed(cards) { _, card ->
                    CardListItem(
                        card = card,
                        onClick = { onCardClick(card) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))

                    /// Card Count Footer
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
        CardListScreen()
    }
}
