package com.toyprojects.card_pilot.ui.feature.home.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.toyprojects.card_pilot.model.Transaction
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun TransactionItem(
    transaction: Transaction,
    isRevealed: Boolean = false,
    onRevealChange: (Boolean) -> Unit = {},
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    val density = LocalDensity.current
    val buttonWidthDp = 70.dp
    val buttonWidthPx = with(density) { buttonWidthDp.toPx() }
    val maxSwipePx = buttonWidthPx * 2

    val offsetX = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(isRevealed) {
        if (!isRevealed && offsetX.value != 0f) {
            offsetX.animateTo(0f, tween(300))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clipToBounds()
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragStart = {
                        onRevealChange(true)
                    },
                    onDragEnd = {
                        coroutineScope.launch {
                            if (offsetX.value < -maxSwipePx / 2) {
                                offsetX.animateTo(-maxSwipePx, tween(300))
                                onRevealChange(true)
                            } else {
                                offsetX.animateTo(0f, tween(300))
                                onRevealChange(false)
                            }
                        }
                    },
                    onDragCancel = {
                        coroutineScope.launch {
                            offsetX.animateTo(0f, tween(300))
                            onRevealChange(false)
                        }
                    },
                    onHorizontalDrag = { change, dragAmount ->
                        val newOffset = (offsetX.value + dragAmount).coerceIn(-maxSwipePx, 0f)
                        coroutineScope.launch {
                            offsetX.snapTo(newOffset)
                        }
                    }
                )
            }
    ) {
        // Actions Layer
        Row(
            modifier = Modifier
                .matchParentSize()
                .offset { IntOffset((offsetX.value + maxSwipePx).roundToInt(), 0) },
            horizontalArrangement = Arrangement.End
        ) {
            // Edit Button
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(buttonWidthDp)
                    .background(CardPilotColors.Secondary)
                    .clickable {
                        onRevealChange(false)
                        onEdit()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "수정",
                    style = MaterialTheme.typography.labelMedium,
                    color = CardPilotColors.White
                )
            }

            // Delete Button
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(buttonWidthDp)
                    .background(CardPilotColors.Error)
                    .clickable {
                        onRevealChange(false)
                        onDelete()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "삭제",
                    style = MaterialTheme.typography.labelMedium,
                    color = CardPilotColors.White
                )
            }
        }

        // Foreground Layer
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .background(Color.Transparent)
                .padding(vertical = 16.dp, horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            /// Date and Time
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.width(50.dp)
            ) {
                Text(
                    text = transaction.date,
                    style = MaterialTheme.typography.bodyMedium,
                    color = CardPilotColors.TextPrimary,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = transaction.time,
                    style = MaterialTheme.typography.labelSmall,
                    color = CardPilotColors.Secondary
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                /// Merchant
                Text(
                    text = transaction.merchant,
                    style = MaterialTheme.typography.bodyMedium,
                    color = CardPilotColors.TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )

                /// Amount
                val amountText = buildAnnotatedString {
                    append("%,d원".format(transaction.amount))
                    if (transaction.eligible != null && transaction.eligible < transaction.amount) {
                        withStyle(style = SpanStyle(color = CardPilotColors.Secondary)) {
                            append(" (적용 금액 %,d원)".format(transaction.eligible))
                        }
                    }
                }

                Text(
                    text = amountText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = CardPilotColors.TextPrimary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionItemPreview() {
    CardPilotTheme {
        TransactionItem(
            transaction = Transaction(
                id = 1,
                merchant = "Starbucks",
                date = "02.14",
                time = "12:30",
                amount = 5600L,
                monthGroup = "2026년 2월"
            ),
            onEdit = {},
            onDelete = {}
        )
    }
}
