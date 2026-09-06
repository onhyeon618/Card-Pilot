package com.toyprojects.card_pilot.ui.feature.transaction.components

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toyprojects.card_pilot.ui.theme.CardPilotColors
import com.toyprojects.card_pilot.ui.theme.CardPilotTheme
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.roundToInt
import kotlin.math.sin

private val DIAL_SIZE_DP = 256.dp
private const val NUMBER_RADIUS_RATIO = 0.75f
private const val INNER_RADIUS_RATIO = 0.45f

@Composable
fun CardPilotTimePicker(
    hour: Int,
    minute: Int,
    onHourChange: (Int) -> Unit,
    onMinuteChange: (Int) -> Unit
) {
    var isHourMode by remember { mutableStateOf(true) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(width = 96.dp, height = 80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isHourMode) CardPilotColors.cta.copy(alpha = 0.2f) else CardPilotColors.gray100)
                    .clickable { isHourMode = true },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "%02d".format(hour),
                    style = MaterialTheme.typography.displayLarge,
                    color = if (isHourMode) CardPilotColors.cta else CardPilotColors.textPrimary
                )
            }
            Text(
                text = ":",
                style = MaterialTheme.typography.displayLarge,
                color = CardPilotColors.textPrimary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp)
            )
            Box(
                modifier = Modifier
                    .size(width = 96.dp, height = 80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (!isHourMode) CardPilotColors.cta.copy(alpha = 0.2f) else CardPilotColors.gray100)
                    .clickable { isHourMode = false },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "%02d".format(minute),
                    style = MaterialTheme.typography.displayLarge,
                    color = if (!isHourMode) CardPilotColors.cta else CardPilotColors.textPrimary
                )
            }
        }

        // 스냅 효과를 구현하기 위해 다이얼 커스텀 구현
        CardPilotStrictClockDial(
            value = if (isHourMode) hour else minute,
            isHour = isHourMode,
            onChange = { newValue ->
                if (isHourMode) onHourChange(newValue) else onMinuteChange(newValue)
            }
        )
    }
}

@Composable
private fun CardPilotStrictClockDial(
    value: Int,
    isHour: Boolean,
    onChange: (Int) -> Unit
) {
    val view = LocalView.current
    val density = LocalDensity.current
    val radiusPx = with(density) { (DIAL_SIZE_DP / 2).toPx() }
    val numberRadiusPx = radiusPx * NUMBER_RADIUS_RATIO
    val innerRadiusPx = radiusPx * INNER_RADIUS_RATIO

    val gray100 = CardPilotColors.gray100
    val ctaColor = CardPilotColors.cta
    val textPrimary = CardPilotColors.textPrimary
    val textSecondary = CardPilotColors.textSecondary

    Box(
        modifier = Modifier
            .size(DIAL_SIZE_DP)
            .background(gray100, CircleShape)
            .pointerInput(isHour) {
                detectTapGestures { offset ->
                    val newValue = calculateTimeFromAngle(offset.x, offset.y, radiusPx, numberRadiusPx, innerRadiusPx, isHour)
                    if (newValue != value) {
                        view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
                        onChange(newValue)
                    }
                }
            }
            .pointerInput(isHour) {
                var currentDragValue = -1
                detectDragGestures(
                    onDragStart = { offset ->
                        currentDragValue = calculateTimeFromAngle(offset.x, offset.y, radiusPx, numberRadiusPx, innerRadiusPx, isHour)
                        if (currentDragValue != value) {
                            view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
                            onChange(currentDragValue)
                        }
                    },
                    onDragEnd = { currentDragValue = -1 },
                    onDragCancel = { currentDragValue = -1 }
                ) { change, _ ->
                    change.consume()
                    val newValue = calculateTimeFromAngle(change.position.x, change.position.y, radiusPx, numberRadiusPx, innerRadiusPx, isHour)
                    if (currentDragValue == -1) currentDragValue = value

                    if (newValue != currentDragValue) {
                        view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
                        currentDragValue = newValue
                        onChange(newValue)
                    }
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerOffset = Offset(size.width / 2, size.height / 2)

            val angle = if (isHour) {
                val hour12 = value % 12
                hour12 * 30 - 90
            } else {
                value * 6 - 90
            }

            val radian = angle * PI / 180
            val distance = if (isHour && (value == 0 || value > 12)) innerRadiusPx else numberRadiusPx
            val selectedX = centerOffset.x + (cos(radian) * distance).toFloat()
            val selectedY = centerOffset.y + (sin(radian) * distance).toFloat()
            val selectedOffset = Offset(selectedX, selectedY)

            /// 시계 중앙 점
            drawCircle(color = ctaColor, radius = 4.dp.toPx(), center = centerOffset)

            /// 중앙에서 핸들까지 이어진 선
            drawLine(
                color = ctaColor,
                start = centerOffset,
                end = selectedOffset,
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )

            /// 핸들
            drawCircle(color = ctaColor, radius = 20.dp.toPx(), center = selectedOffset)

            /// 핸들 중앙의 하얀 점 - 5분 단위일 경우 점 대신 숫자만 노출
            if (!isHour && value % 5 != 0) {
                drawCircle(color = Color.White, radius = 2.dp.toPx(), center = selectedOffset)
            }
        }

        val numbers = remember(isHour) {
            if (isHour) (0..23).toList() else (0..59 step 5).toList()
        }

        val selectedAngle = if (isHour) ((value % 12) * 30 - 90) else (value * 6 - 90)
        val selectedRadian = selectedAngle * PI / 180
        val selectedDistance = if (isHour && (value == 0 || value > 12)) innerRadiusPx else numberRadiusPx
        val selectedX = radiusPx + (cos(selectedRadian) * selectedDistance).toFloat()
        val selectedY = radiusPx + (sin(selectedRadian) * selectedDistance).toFloat()

        numbers.forEach { num ->
            val isInner = isHour && (num == 0 || num > 12)
            val angle = if (isHour) (num % 12) * 30 - 90 else num * 6 - 90
            val radian = angle * PI / 180
            val distance = if (isInner) innerRadiusPx else numberRadiusPx
            val x = radiusPx + (cos(radian) * distance).toFloat()
            val y = radiusPx + (sin(radian) * distance).toFloat()

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.TopStart)
                    .offset(
                        x = with(density) { x.toDp() } - 20.dp,
                        y = with(density) { y.toDp() } - 20.dp
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (num == 0 && isHour) "00" else num.toString(),
                    color = if (isInner) textSecondary else textPrimary,
                    fontSize = if (isInner) 13.sp else 15.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        // 숫자 중 핸들에 겹치는 부분은 흰색으로 표시
        Box(
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.TopStart)
                .offset(
                    x = with(density) { selectedX.toDp() } - 20.dp,
                    y = with(density) { selectedY.toDp() } - 20.dp
                )
                .clip(CircleShape)
        ) {
            numbers.forEach { num ->
                val isInner = isHour && (num == 0 || num > 12)
                val angle = if (isHour) (num % 12) * 30 - 90 else num * 6 - 90
                val radian = angle * PI / 180
                val distance = if (isInner) innerRadiusPx else numberRadiusPx
                val x = radiusPx + (cos(radian) * distance).toFloat()
                val y = radiusPx + (sin(radian) * distance).toFloat()

                val overlapDistance = hypot((x - selectedX).toDouble(), (y - selectedY).toDouble())
                if (overlapDistance < with(density) { 40.dp.toPx() }) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .offset(
                                x = with(density) { (x - selectedX).toDp() },
                                y = with(density) { (y - selectedY).toDp() }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (num == 0 && isHour) "00" else num.toString(),
                            color = Color.White,
                            fontSize = if (isInner) 13.sp else 15.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

private fun calculateTimeFromAngle(
    x: Float,
    y: Float,
    radiusPx: Float,
    numberRadiusPx: Float,
    innerRadiusPx: Float,
    isHour: Boolean
): Int {
    var angle = atan2(y - radiusPx, x - radiusPx) * 180 / PI
    angle += 90
    if (angle < 0) angle += 360

    return if (isHour) {
        var hour = (angle / 30).roundToInt() % 12
        if (hour == 0) hour = 12

        val distance = hypot((x - radiusPx).toDouble(), (y - radiusPx).toDouble())
        val isInnerRing = distance < (numberRadiusPx + innerRadiusPx) / 2

        if (isInnerRing) {
            if (hour == 12) 0 else hour + 12
        } else {
            if (hour == 12) 12 else hour
        }
    } else {
        (angle / 6).roundToInt() % 60
    }
}

@Preview(showBackground = true)
@Composable
private fun CardPilotTimePickerPreview() {
    CardPilotTheme {
        CardPilotTimePicker(
            hour = 14,
            minute = 30,
            onHourChange = {},
            onMinuteChange = {}
        )
    }
}
