package com.toyprojects.card_pilot.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RippleConfiguration
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.toyprojects.card_pilot.ui.components.BenefitItemRow
import com.toyprojects.card_pilot.ui.components.GlassScaffold
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCardScreen(
    onBack: () -> Unit = {},
    onSave: () -> Unit = {},
    onEditBenefit: (Int, String, String) -> Unit = { _, _, _ -> }
) {
    var cardName by remember { mutableStateOf("") }
    var benefits by remember {
        mutableStateOf(
            listOf(
                Pair("여행 (Travel)", "여행 혜택"),
                Pair("주유 (Gas)", "주유 혜택"),
                Pair("쇼핑 (Shopping)", "쇼핑 혜택")
            )
        )
    }

    GlassScaffold(
        topBar = {
            TopAppBar(
                title = { },
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            contentPadding = PaddingValues(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            ),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            /// Header & Card Preview Input
            item {
                Column {
                    Text(
                        text = "새로운 카드 등록",
                        style = MaterialTheme.typography.headlineMedium,
                        color = CardPilotColors.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    // TODO: use card image as background
                    CompositionLocalProvider(
                        LocalRippleConfiguration provides RippleConfiguration(color = CardPilotColors.White)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .shadow(
                                    elevation = 8.dp,
                                    shape = RoundedCornerShape(16.dp),
                                    spotColor = Color(0x33000000)
                                )
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = CardPilotColors.PastelGradientColors
                                    )
                                )
                                .clickable {
                                    // TODO: implement image pick logic
                                }
                                .padding(24.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                Column {
                                    Text(
                                        text = "CARD NAME",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = CardPilotColors.Violet800
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    BasicTextField(
                                        value = cardName,
                                        onValueChange = { cardName = it },
                                        textStyle = MaterialTheme.typography.headlineSmall.copy(
                                            color = CardPilotColors.Violet900
                                        ),
                                        decorationBox = { innerTextField ->
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(
                                                        color = CardPilotColors.White.copy(alpha = 0.2f),
                                                        shape = RoundedCornerShape(2.dp)
                                                    )
                                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                                contentAlignment = Alignment.CenterStart
                                            ) {
                                                if (cardName.isEmpty()) {
                                                    Text(
                                                        text = "카드 이름 입력",
                                                        style = MaterialTheme.typography.headlineSmall,
                                                        color = CardPilotColors.Secondary
                                                    )
                                                }
                                                innerTextField()
                                            }
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }
                }
            }

            /// Benefits header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "혜택",
                        style = MaterialTheme.typography.titleLarge,
                        color = CardPilotColors.TextPrimary
                    )
                    CompositionLocalProvider(
                        LocalRippleConfiguration provides RippleConfiguration(color = CardPilotColors.GradientPeach)
                    ) {
                        FilledTonalButton(
                            onClick = {
                                onEditBenefit(-1, "", "")
                            },
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = CardPilotColors.SurfaceCard,
                                contentColor = CardPilotColors.TextPrimary
                            ),
                            shape = CircleShape,
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("추가", style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            }

            /// Benefits list
            itemsIndexed(benefits) { index, benefit ->
                BenefitItemRow(
                    name = benefit.first,
                    description = benefit.second,
                    onClick = {
                        onEditBenefit(index, benefit.first, benefit.second)
                    },
                    onDelete = {
                        // TODO: implement logic
                    }
                )
            }

            /// Save Button
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onSave,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(
                            8.dp,
                            RoundedCornerShape(16.dp),
                            spotColor = CardPilotColors.Primary.copy(alpha = 0.3f)
                        ),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CardPilotColors.SoftSlateIndigo,
                        contentColor = CardPilotColors.White
                    )
                ) {
                    Text(
                        "카드 등록 완료",
                        style = MaterialTheme.typography.titleMedium.copy(color = CardPilotColors.White)
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Preview
@Composable
fun AddCardScreenPreview() {
    CardPilotTheme {
        AddCardScreen()
    }
}
