package com.toyprojects.card_pilot.ui.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.toyprojects.card_pilot.R
import com.toyprojects.card_pilot.ui.shared.CardPilotRipple
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme

@Composable
fun CardDropdownEmpty(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .background(CardPilotColors.surfaceGlass, RoundedCornerShape(24.dp))
            .border(1.dp, CardPilotColors.outline, RoundedCornerShape(24.dp))
    ) {
        CardPilotRipple {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .clickable(
                        role = Role.Button,
                        onClickLabel = stringResource(R.string.desc_add_card),
                        onClick = onClick
                    )
                    .semantics(mergeDescendants = true) {}
                    .padding(vertical = 20.dp, horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                /// 빈 카드 플레이스홀더
                Box(
                    modifier = Modifier
                        .size(width = 48.dp, height = 30.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = CardPilotColors.pastelGradientColors
                            ),
                            RoundedCornerShape(6.dp)
                        )
                )

                Spacer(modifier = Modifier.width(16.dp))

                /// 카드 없음 안내 텍스트
                Text(
                    text = stringResource(R.string.msg_no_card),
                    style = MaterialTheme.typography.titleMedium,
                    color = CardPilotColors.textSecondary
                )
            }
        }
    }
}

@Preview
@Composable
fun EmptyCardButtonPreview() {
    CardPilotTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            CardDropdownEmpty(onClick = {})
        }
    }
}