package com.hana.fieldmate.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.hana.fieldmate.ui.theme.LineDBDBDB
import com.hana.fieldmate.ui.theme.Shapes

@Composable
fun FDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = { },
    content: @Composable () -> Unit,
    button: @Composable () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = Shapes.large,
            color = Color.White
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content()

                Spacer(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(LineDBDBDB)
                )

                button()
            }
        }
    }
}