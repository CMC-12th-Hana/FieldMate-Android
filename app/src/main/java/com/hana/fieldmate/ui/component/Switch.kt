package com.hana.fieldmate.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hana.fieldmate.ui.theme.BgD3D3D3
import com.hana.fieldmate.ui.theme.Main356DF8

@Composable
fun FSwitch(
    width: Dp = 40.dp,
    height: Dp = 22.dp,
    thumbSize: Dp = 15.dp,
    checkedTrackColor: Color = Main356DF8,
    uncheckedTrackColor: Color = BgD3D3D3,
    thumbColor: Color = Color.White,
    switchOn: Boolean = false,
    switchOnClick: (Boolean) -> Unit,
    gapBetweenThumbAndTrackEdge: Dp = 4.dp
) {
    val interactionSource = remember { MutableInteractionSource() }
    val alignment by animateHorizontalAlignmentAsState(if (switchOn) 1f else -1f)

    Box(
        modifier = Modifier
            .size(width, height)
            .background(
                color = if (switchOn) checkedTrackColor else uncheckedTrackColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(
                indication = null,
                interactionSource = interactionSource,
                onClick = { switchOnClick(!switchOn) }
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .padding(
                    start = gapBetweenThumbAndTrackEdge,
                    end = gapBetweenThumbAndTrackEdge
                )
                .fillMaxSize(),
            contentAlignment = alignment
        ) {
            Spacer(
                modifier = Modifier
                    .size(thumbSize)
                    .background(
                        color = thumbColor,
                        shape = CircleShape
                    )
            )
        }
    }
}

@Composable
private fun animateHorizontalAlignmentAsState(
    targetBiasValue: Float
): State<BiasAlignment> {
    val bias by animateFloatAsState(targetBiasValue)
    return derivedStateOf { BiasAlignment(bias, 0f) }
}