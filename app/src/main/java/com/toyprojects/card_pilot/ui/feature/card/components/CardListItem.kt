package com.toyprojects.card_pilot.ui.feature.card.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.toyprojects.card_pilot.model.CardSimpleInfo
import com.toyprojects.card_pilot.ui.shared.CardPilotRipple
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme
import sh.calvin.reorderable.ReorderableCollectionItemScope

@Composable
fun ReorderableCollectionItemScope.CardListItem(
    card: CardSimpleInfo,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    CardPilotRipple {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(CardPilotColors.SurfaceGlass, RoundedCornerShape(24.dp))
                .border(1.dp, CardPilotColors.Outline, RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
                .clickable(onClick = onClick)
                .padding(horizontal = 24.dp, vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            /// Drag Handle
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "끌어서 순서 바꾸기",
                tint = CardPilotColors.Secondary.copy(alpha = 0.5f),
                modifier = Modifier.draggableHandle()
            )

            Spacer(modifier = Modifier.width(16.dp))

            /// Card image placeholder
            // TODO: use actual card image
            Box(
                modifier = Modifier
                    .height(30.dp)
                    .aspectRatio(1.58f)
                    .background(
                        brush = Brush.linearGradient(
                            colors = CardPilotColors.PastelGradientColors
                        ),
                        shape = RoundedCornerShape(4.dp)
                    )
            ) {
                if (card.image.isNotEmpty()) {
                    AsyncImage(
                        model = card.image,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(4.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            /// 카드 이름
            Text(
                text = card.name,
                style = MaterialTheme.typography.titleMedium,
                color = CardPilotColors.TextPrimary,
                modifier = Modifier.weight(1f)
            )

            /// 화살표 아이콘
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = CardPilotColors.Secondary.copy(alpha = 0.5f)
            )
        }
    }
}
