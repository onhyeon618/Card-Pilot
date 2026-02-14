package com.toyprojects.card_pilot.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.toyprojects.card_pilot.ui.screens.CardInfo
import com.toyprojects.card_pilot.ui.theme.Gray300
import com.toyprojects.card_pilot.ui.theme.Primary
import com.toyprojects.card_pilot.ui.theme.Secondary
import com.toyprojects.card_pilot.ui.theme.SurfaceCard
import com.toyprojects.card_pilot.ui.theme.TextPrimary

@Composable
fun CardListItem(
    card: CardInfo,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = Color(0x1A000000)
            )
            .background(SurfaceCard, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        /// Drag Handle
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "끌어서 순서 바꾸기",
            tint = Secondary.copy(alpha = 0.5f)
        )

        Spacer(modifier = Modifier.width(12.dp))

        /// Card image placeholder
        // TODO: use actual card image
        Box(
            modifier = Modifier
                .size(width = 48.dp, height = 30.dp)
                .background(Primary, RoundedCornerShape(4.dp))
        )

        Spacer(modifier = Modifier.width(16.dp))

        /// Card name
        Text(
            text = card.name,
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary,
            modifier = Modifier.weight(1f)
        )

        /// Arrow icon
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Gray300
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun CardListItemPreview() {
    com.toyprojects.card_pilot.ui.theme.CardPilotTheme {
        CardListItem(
            card = CardInfo("The Red", ""),
            onClick = { }
        )
    }
}
