package com.toyprojects.card_pilot.ui.feature.card.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import coil.request.ImageRequest
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
    var textColor by remember { mutableStateOf(CardPilotColors.Violet900) }
    var labelColor by remember { mutableStateOf(CardPilotColors.Violet800) }
    var hintColor by remember { mutableStateOf(CardPilotColors.Secondary) }

    CardPilotRipple(color = CardPilotColors.White) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(1.58f)
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
                val imageRequest = ImageRequest.Builder(LocalContext.current)
                    .data(cardImage)
                    .allowHardware(false)
                    .build()

                AsyncImage(
                    model = imageRequest,
                    contentDescription = "Card Background",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    onSuccess = { success ->
                        val drawable = success.result.drawable
                        val width = drawable.intrinsicWidth
                        val height = drawable.intrinsicHeight
                        val bitmap = if (width > 0 && height > 0) drawable.toBitmap() else null

                        // 카드 이미지 색상에 따라 텍스트 색상 결정
                        bitmap?.let {
                            Palette.from(it).generate { palette ->
                                val swatch =
                                    palette?.dominantSwatch ?: palette?.lightVibrantSwatch ?: palette?.vibrantSwatch
                                swatch?.let { validSwatch ->
                                    val backgroundColor = Color(validSwatch.rgb)
                                    val isLightBackground = backgroundColor.luminance() > 0.5f
                                    val baseTextColor = if (isLightBackground) Color.Black else Color.White

                                    textColor = baseTextColor
                                    labelColor = baseTextColor.copy(alpha = 0.8f)
                                    hintColor = baseTextColor.copy(alpha = 0.5f)
                                }
                            }
                        }
                    }
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
                        color = labelColor
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    BasicTextField(
                        value = cardName,
                        onValueChange = onNameChange,
                        textStyle = MaterialTheme.typography.headlineSmall.copy(
                            color = textColor
                        ),
                        decorationBox = { innerTextField ->
                            if (cardName.isEmpty()) {
                                Text(
                                    text = "카드 이름 입력",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = hintColor
                                )
                            }
                            innerTextField()
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
