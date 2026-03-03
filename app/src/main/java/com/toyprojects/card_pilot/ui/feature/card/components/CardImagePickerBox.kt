package com.toyprojects.card_pilot.ui.feature.card.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.toyprojects.card_pilot.ui.shared.CardPilotRipple
import com.toyprojects.card_pilot.ui.theme.CardPilotColors

@Composable
fun CardImagePickerBox(
    cardName: String,
    cardImage: String,
    onNameChange: (String) -> Unit,
    onImageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CardPilotRipple(color = CardPilotColors.White) {
        Box(
            modifier = modifier
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
                .clickable(onClick = onImageClick)
        ) {
            if (cardImage.isNotEmpty()) {
                AsyncImage(
                    model = cardImage,
                    contentDescription = "Card Background",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                /// 카드 이름 입력창
                Column {
                    Text(
                        text = "CARD NAME",
                        style = MaterialTheme.typography.labelSmall,
                        color = CardPilotColors.Violet800
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    BasicTextField(
                        value = cardName,
                        onValueChange = onNameChange,
                        textStyle = MaterialTheme.typography.headlineSmall.copy(
                            color = CardPilotColors.Violet900
                        ),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = CardPilotColors.White.copy(
                                            alpha = 0.2f
                                        ),
                                        shape = RoundedCornerShape(2.dp)
                                    )
                                    .padding(
                                        horizontal = 8.dp,
                                        vertical = 4.dp
                                    ),
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
