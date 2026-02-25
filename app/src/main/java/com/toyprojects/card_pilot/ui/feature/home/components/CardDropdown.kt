package com.toyprojects.card_pilot.ui.feature.home.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.toyprojects.card_pilot.model.CardSimpleInfo
import com.toyprojects.card_pilot.ui.shared.CardPilotRipple
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme

@Composable
fun CardDropdown(
    selectedCard: CardSimpleInfo?,
    cardList: List<CardSimpleInfo>,
    onCardSelected: (Long) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var dropdownWidth by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    val rotationState by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "Dropdown Arrow Rotation"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .onSizeChanged {
                dropdownWidth = with(density) { it.width.toDp() }
            }
            .background(CardPilotColors.SurfaceGlass, RoundedCornerShape(24.dp))
            .border(1.dp, CardPilotColors.Outline, RoundedCornerShape(24.dp))
    ) {
        CardPilotRipple {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .clickable { expanded = !expanded }
                    .padding(vertical = 20.dp, horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                // TODO: 실제 카드 이미지 연동
                /// Card image placeholder
                Box(
                    modifier = Modifier
                        .size(width = 48.dp, height = 30.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = CardPilotColors.PastelGradientColors
                            ),
                            RoundedCornerShape(6.dp)
                        )
                )

                Spacer(modifier = Modifier.width(16.dp))

                /// 카드 이름
                Text(
                    text = selectedCard?.name ?: "카드 선택",
                    style = MaterialTheme.typography.titleMedium,
                    color = CardPilotColors.TextPrimary,
                    modifier = Modifier.weight(1f)
                )

                /// 드롭다운 화살표 아이콘
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Expand",
                    tint = CardPilotColors.Secondary,
                    modifier = Modifier
                        .rotate(rotationState)
                        .size(24.dp)
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(dropdownWidth),
            containerColor = lerp(
                CardPilotColors.PastelViolet,
                CardPilotColors.White,
                0.8f
            ),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, CardPilotColors.Outline)
        ) {
            cardList.forEach { card ->
                CardPilotRipple {
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 20.dp)
                            ) {
                                // TODO: 카드 이미지 적용
                                /// 카드 이미지
                                Box(
                                    modifier = Modifier
                                        .size(width = 32.dp, height = 20.dp)
                                        .background(
                                            brush = Brush.linearGradient(
                                                colors = CardPilotColors.PastelGradientColors
                                            ),
                                            RoundedCornerShape(4.dp)
                                        )
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                /// 카드 이름
                                Text(
                                    text = card.name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = CardPilotColors.TextPrimary
                                )
                            }
                        },
                        onClick = {
                            onCardSelected(card.id)
                            expanded = false
                        },
                        colors = MenuDefaults.itemColors(
                            textColor = CardPilotColors.TextPrimary,
                        )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun CardDropdownPreview() {
    CardPilotTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            CardDropdown(
                selectedCard = CardSimpleInfo(1L, "현대카드 The Red", "", 0),
                cardList = listOf(
                    CardSimpleInfo(1L, "현대카드 The Red", "", 0),
                    CardSimpleInfo(2L, "삼성카드 taptap O", "", 1),
                    CardSimpleInfo(3L, "신한카드 Mr.Life", "", 2),
                ),
                onCardSelected = {}
            )
        }
    }
}
