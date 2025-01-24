package com.arash.altafi.chatandroid.utils.ext

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.drawBehind

fun Modifier.borderBottom(
    color: Color = Color.White,
    strokeWidth: Dp = 2.dp
): Modifier = this.drawBehind {
    val strokePx = strokeWidth.toPx()
    drawLine(
        color = color,
        start = Offset(x = 0f, y = size.height - strokePx / 2),
        end = Offset(x = size.width, y = size.height - strokePx / 2),
        strokeWidth = strokePx
    )
}
