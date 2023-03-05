package com.hana.fieldmate.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Composable
fun RoundedLinearProgressBar(
    /*@FloatRange(from = 0.0, to = 1.0)*/
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
    height: Dp = 10.dp
) {
    Canvas(modifier = modifier) {
        drawRoundedLinearProgressBar(
            startFraction = 0f,
            endFraction = progress,
            height = height,
            color = color
        )
    }
}

private fun DrawScope.drawRoundedLinearProgressBar(
    startFraction: Float,
    endFraction: Float,
    height: Dp,
    color: Color,
) {
    val width = size.width
    // Start drawing from the vertical center of the stroke
    val yOffset = 0f

    val isLtr = layoutDirection == LayoutDirection.Ltr
    val barStart = (if (isLtr) startFraction else 1f - endFraction) * width
    val barEnd = (if (isLtr) endFraction else 1f - startFraction) * width

    // Progress line
    drawRoundRect(
        color = color,
        topLeft = Offset(barStart, yOffset),
        size = Size(barEnd, height.toPx()),
        cornerRadius = CornerRadius(4.dp.toPx())
    )
}