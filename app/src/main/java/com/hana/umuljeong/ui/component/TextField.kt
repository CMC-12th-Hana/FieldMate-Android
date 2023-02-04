package com.hana.umuljeong.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hana.umuljeong.ui.theme.FontBlack
import com.hana.umuljeong.ui.theme.FontLightGray
import com.hana.umuljeong.ui.theme.LineLightGray

@Composable
fun UTextField(
    modifier: Modifier = Modifier,
    msgContent: String,
    hint: String,
    onValueChange: (String) -> Unit = { },
) {
    val focusRequester = remember { FocusRequester() }
    var hintMsg by remember { mutableStateOf(hint) }

    BasicTextField(
        value = msgContent,
        onValueChange = onValueChange,
        modifier = modifier
            .focusRequester(focusRequester = focusRequester)
            .onFocusChanged {
                if (it.isFocused) {
                    if (msgContent.isEmpty()) hintMsg = ""
                } else {
                    hintMsg = if (msgContent.isEmpty()) hint else ""
                }
            }
        ,
        textStyle = TextStyle(
            fontSize = 16.sp,
            color = FontBlack
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier = modifier
                    .background(
                        color = White,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = LineLightGray,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(
                        all = 16.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = hintMsg,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = FontLightGray
                )
                innerTextField()
            }
        }
    )
}