package com.toyprojects.card_pilot.ui.feature.card.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

// Thinner version of delete outline icon. 
// Uses SVG path from Material Symbols Light (approx 300 weight)
val DeleteThin: ImageVector
    get() {
        if (_deleteThin != null) {
            return _deleteThin!!
        }
        _deleteThin = ImageVector.Builder(
            name = "DeleteThin",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            // Material Symbols uses viewBox "0 -960 960 960"
            // Compose `ImageVector` viewport starts at (0, 0)
            // So we translate Y by +960 to bring it into the visible viewport
            group(
                name = "translateGroup",
                translationY = 960f
            ) {
                path(
                    fill = SolidColor(Color.Black),
                    fillAlpha = 1.0f,
                    stroke = null,
                    strokeAlpha = 1.0f,
                    strokeLineWidth = 1.0f,
                    strokeLineCap = StrokeCap.Butt,
                    strokeLineJoin = StrokeJoin.Miter,
                    strokeLineMiter = 1.0f,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(292.31f, -140f)
                    quadToRelative(-29.92f, 0f, -51.12f, -21.19f)
                    quadTo(220f, -182.39f, 220f, -212.31f)
                    verticalLineTo(-720f)
                    horizontalLineToRelative(-40f)
                    verticalLineToRelative(-60f)
                    horizontalLineToRelative(180f)
                    verticalLineToRelative(-35.38f)
                    horizontalLineToRelative(240f)
                    verticalLineTo(-780f)
                    horizontalLineToRelative(180f)
                    verticalLineToRelative(60f)
                    horizontalLineToRelative(-40f)
                    verticalLineToRelative(507.69f)
                    quadTo(740f, -182f, 719f, -161f)
                    quadToRelative(-21f, 21f, -51.31f, 21f)
                    horizontalLineTo(292.31f)
                    close()
                    moveTo(680f, -720f)
                    horizontalLineTo(280f)
                    verticalLineToRelative(507.69f)
                    quadToRelative(0f, 5.39f, 3.46f, 8.85f)
                    reflectiveQuadToRelative(8.85f, 3.46f)
                    horizontalLineToRelative(375.38f)
                    quadToRelative(4.62f, 0f, 8.46f, -3.85f)
                    quadToRelative(3.85f, -3.84f, 3.85f, -8.46f)
                    verticalLineTo(-720f)
                    close()
                    moveTo(376.16f, -280f)
                    horizontalLineToRelative(59.99f)
                    verticalLineToRelative(-360f)
                    horizontalLineToRelative(-59.99f)
                    verticalLineToRelative(360f)
                    close()
                    moveToRelative(147.69f, 0f)
                    horizontalLineToRelative(59.99f)
                    verticalLineToRelative(-360f)
                    horizontalLineToRelative(-59.99f)
                    verticalLineToRelative(360f)
                    close()
                    moveTo(280f, -720f)
                    verticalLineToRelative(520f)
                    verticalLineToRelative(-520f)
                    close()
                }
            }
        }.build()
        return _deleteThin!!
    }

private var _deleteThin: ImageVector? = null
