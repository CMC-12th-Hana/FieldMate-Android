package com.hana.umuljeong.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hana.umuljeong.R
import com.hana.umuljeong.ui.theme.*

@Composable
fun UTextField(
    modifier: Modifier = Modifier,
    msgContent: String,
    hint: String = "",
    readOnly: Boolean = false,
    textStyle: TextStyle = TextStyle(
        color = FontBlack,
        fontSize = 16.sp
    ),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = true,
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
            },
        readOnly = readOnly,
        singleLine = singleLine,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
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
                    )
            ) {
                Text(
                    text = hintMsg,
                    color = FontLightGray
                )
                innerTextField()
            }
        }
    )
}

@Composable
fun UTextFieldWithTitle(
    modifier: Modifier = Modifier,
    msgContent: String,
    title: String,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    textStyle: TextStyle = TextStyle(
        color = FontDarkGray,
        fontSize = 16.sp
    ),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onValueChange: (String) -> Unit = { },
) {
    BasicTextField(
        value = msgContent,
        onValueChange = onValueChange,
        modifier = modifier,
        readOnly = readOnly,
        singleLine = singleLine,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
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
                        top = 18.dp,
                        bottom = 18.dp,
                        start = 20.dp,
                        end = 20.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    color = FontBlack
                )

                Spacer(modifier = Modifier.width(10.dp))

                Surface(
                    modifier = Modifier.padding(bottom = 1.dp)
                ) {
                    innerTextField()
                }
            }
        }
    )
}

@Composable
fun USearchTextField(
    modifier: Modifier = Modifier,
    msgContent: String,
    hint: String = "",
    readOnly: Boolean = false,
    textStyle: TextStyle = TextStyle(
        color = FontBlack,
        fontSize = 14.sp
    ),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
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
            },
        readOnly = readOnly,
        singleLine = singleLine,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        decorationBox = { innerTextField ->
            Row(
                modifier = modifier
                    .background(
                        color = BgGray,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(
                        top = 10.dp, bottom = 10.dp, start = 20.dp, end = 20.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    tint = Color.Unspecified,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = hintMsg,
                    style = TextStyle(
                        color = FontLightGray,
                        fontSize = 14.sp
                    )
                )

                innerTextField()
            }
        }
    )
}

@Preview
@Composable
fun PreviewUTextFieldWithHint() {
    UmuljeongTheme {
        UTextField(
            modifier = Modifier.width(335.dp),
            msgContent = "",
            hint = stringResource(id = R.string.pw_input_hint),
            onValueChange = { }
        )
    }
}

@Preview
@Composable
fun PreviewUTextFieldWithTitle() {
    UmuljeongTheme {
        UTextFieldWithTitle(
            modifier = Modifier.width(335.dp),
            msgContent = "황진하",
            title = stringResource(id = R.string.customer_name),
            onValueChange = { }
        )
    }
}

@Preview
@Composable
fun PreviewUSearchTextFieldWithHint() {
    UmuljeongTheme {
        USearchTextField(
            modifier = Modifier.width(335.dp),
            msgContent = "",
            hint = stringResource(id = R.string.search_customer_hint),
            onValueChange = { }
        )
    }
}