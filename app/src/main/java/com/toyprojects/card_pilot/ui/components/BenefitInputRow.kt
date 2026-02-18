package com.toyprojects.card_pilot.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.toyprojects.card_pilot.ui.theme.Gray100
import com.toyprojects.card_pilot.ui.theme.Gray300
import com.toyprojects.card_pilot.ui.theme.OutlineHigh
import com.toyprojects.card_pilot.ui.theme.Secondary
import com.toyprojects.card_pilot.ui.theme.SurfaceGlassHigh
import com.toyprojects.card_pilot.ui.theme.TextPrimary


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BenefitInputRow(
    name: String,
    amount: String,
    onNameChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceGlassHigh)
            .border(1.dp, OutlineHigh, RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        /// Drag Handle
        // TODO: make icon actually work as handle
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "끌어서 순서 바꾸기",
            tint = Secondary.copy(alpha = 0.5f)
        )

        Column(modifier = Modifier.weight(1f)) {
            /// Benefit name
            BasicTextField(
                value = name,
                onValueChange = onNameChange,
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = TextPrimary),
                decorationBox = { innerTextField ->
                    Box {
                        if (name.isEmpty()) {
                            Text("혜택 카테고리 (예: 여행)", color = Secondary)
                        }
                        innerTextField()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Gray100)

            /// Benefit limit
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("혜택 한도: ", style = MaterialTheme.typography.bodyMedium, color = Secondary)
                BasicTextField(
                    value = amount,
                    onValueChange = onAmountChange,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    decorationBox = { innerTextField ->
                        Box {
                            if (amount.isEmpty()) {
                                Text("한도 금액", color = Secondary.copy(alpha = 0.5f))
                            }
                            innerTextField()
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        /// Delete button
        IconButton(onClick = onRemove) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "삭제",
                tint = Gray300
            )
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun BenefitInputRowPreview() {
    com.toyprojects.card_pilot.ui.theme.CardPilotTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            BenefitInputRow(
                name = "Travel",
                amount = "150000",
                onNameChange = {},
                onAmountChange = {},
                onRemove = {}
            )
        }
    }
}
