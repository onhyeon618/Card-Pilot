package com.toyprojects.card_pilot.ui.feature.card.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.toyprojects.card_pilot.R
import com.toyprojects.card_pilot.ui.shared.CardPilotRipple
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import sh.calvin.reorderable.ReorderableCollectionItemScope

@Composable
fun ReorderableCollectionItemScope.BenefitItemRow(
    modifier: Modifier = Modifier,
    name: String,
    description: String? = null,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onMoveUp: (() -> Unit)? = null,
    onMoveDown: (() -> Unit)? = null
) {
    val editBenefitLabel = stringResource(R.string.desc_edit_benefit)

    val customAccessibilityActions = buildList {
        if (onMoveUp != null) {
            add(
                CustomAccessibilityAction(stringResource(R.string.desc_action_up)) {
                    onMoveUp()
                    true
                }
            )
        }
        if (onMoveDown != null) {
            add(
                CustomAccessibilityAction(stringResource(R.string.desc_action_down)) {
                    onMoveDown()
                    true
                }
            )
        }
    }

    CardPilotRipple(color = CardPilotColors.gradientEnd) {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier
                .fillMaxWidth()
                .semantics {
                    onClick(label = editBenefitLabel) {
                        onClick()
                        true
                    }
                    if (customAccessibilityActions.isNotEmpty()) {
                        customActions = customAccessibilityActions
                    }
                },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = CardPilotColors.surfaceGlassButton,
                contentColor = CardPilotColors.textPrimary
            ),
            border = BorderStroke(1.dp, CardPilotColors.outlineButton),
            contentPadding = PaddingValues(start = 16.dp, top = 12.dp, end = 8.dp, bottom = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                /// Drag Handle
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = null,
                    tint = CardPilotColors.secondary.copy(alpha = 0.5f),
                    modifier = Modifier.draggableHandle()
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = CardPilotColors.textPrimary
                    )

                    if (!description.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = CardPilotColors.secondary
                        )
                    }
                }

                CardPilotRipple {
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = DeleteThin,
                            contentDescription = stringResource(R.string.desc_delete_benefit_format, name),
                            tint = CardPilotColors.error
                        )
                    }
                }
            }
        }
    }
}

