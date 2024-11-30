package com.smartshop.ui.modifiers

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.borderTop(height: Dp, color: Color, cornerRadius: Dp = 0.dp, cornerRadiusX: Dp = 0.dp, cornerRadiusY: Dp = 0.dp) = composed {
    val density = LocalDensity.current
    val heightPx = density.run { height.toPx() }
    val cornerRadiusPx = density.run { cornerRadius.toPx() }
    val cornerRadiusXPx = if (density.run { cornerRadiusX.toPx() }.toInt() != 0) density.run { cornerRadiusX.toPx() } else cornerRadiusPx
    val cornerRadiusYPx = if (density.run { cornerRadiusY.toPx() }.toInt() != 0) density.run { cornerRadiusY.toPx() } else cornerRadiusPx

    this.drawBehind {
        val width = size.width
        val strokeTop = 0f

        drawRoundRect(
            color = color,
            size = size.copy(width = width, height = heightPx),
            topLeft = Offset(0f, strokeTop),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius.Zero.copy(
                x = cornerRadiusXPx,
                y = cornerRadiusYPx
            )
        )
    }
}
