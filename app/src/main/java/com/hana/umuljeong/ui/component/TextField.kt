package com.hana.umuljeong.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hana.umuljeong.R
import com.hana.umuljeong.getFormattedTime
import com.hana.umuljeong.ui.theme.*
import java.time.LocalDate

@Composable
fun UTextField(
    modifier: Modifier = Modifier,
    msgContent: String,
    hint: String = "",
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = Typography.body3,
    isValid: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = true,
    onValueChange: (String) -> Unit = { },
) {
    val focusRequester = remember { FocusRequester() }
    var initState by rememberSaveable { mutableStateOf(true) }
    var isFocused by rememberSaveable { mutableStateOf(false) }

    if (readOnly) initState = false

    BasicTextField(
        value = msgContent,
        onValueChange = onValueChange,
        modifier = modifier
            .focusRequester(focusRequester = focusRequester)
            .onFocusChanged {
                if (it.isFocused) {
                    if (initState) initState = false
                }
                isFocused = it.isFocused
            },
        readOnly = readOnly,
        singleLine = singleLine,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        decorationBox = { innerTextField ->
            val hintMsg = if (isFocused || msgContent.isNotEmpty()) "" else hint
            val borderColor = if (isFocused) {
                Line191919
            } else {
                if (isValid || initState) LineDBDBDB
                else ErrorFF3120
            }

            TextFieldContainer(
                borderColor = borderColor,
                enabled = enabled,
                hintMsg = hintMsg
            ) {
                innerTextField()
            }
        }
    )
}

@Composable
fun TextFieldContainer(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    borderColor: Color,
    textStyle: TextStyle = Typography.body3,
    color: Color = FontDBDBDB,
    hintMsg: String,
    innerTextField: @Composable () -> Unit
) {
    Row(
        modifier = modifier
            .background(
                color = if (enabled) White else BgF1F1F5,
                shape = Shapes.large
            )
            .border(
                width = 1.dp,
                color = borderColor,
                shape = Shapes.large
            )
            .padding(
                all = 14.dp
            )
    ) {
        Text(
            text = hintMsg,
            style = textStyle,
            color = color
        )
        innerTextField()
    }
}

@Composable
fun UTextFieldWithTimer(
    modifier: Modifier = Modifier,
    msgContent: String,
    hint: String = "",
    remainSeconds: Int,
    readOnly: Boolean = false,
    textStyle: TextStyle = Typography.body3,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = true,
    onValueChange: (String) -> Unit = { },
) {
    val focusRequester = remember { FocusRequester() }
    var hintMsg by rememberSaveable { mutableStateOf(hint) }
    var borderColor = LineDBDBDB

    BasicTextField(
        value = msgContent,
        onValueChange = onValueChange,
        modifier = modifier
            .focusRequester(focusRequester = focusRequester)
            .onFocusChanged {
                if (it.isFocused) {
                    if (msgContent.isEmpty()) hintMsg = ""
                    borderColor = Line191919
                } else {
                    hintMsg = if (msgContent.isEmpty()) hint else ""
                    borderColor = LineDBDBDB
                }
            },
        readOnly = readOnly,
        singleLine = singleLine,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        decorationBox = { innerTextField ->
            TextFieldContainer(
                borderColor = borderColor,
                hintMsg = hintMsg
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    innerTextField()

                    Text(
                        text = getFormattedTime(remainSeconds),
                        color = ErrorFF3120,
                        style = Typography.body4
                    )
                }
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
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        color = Font70747E
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
        keyboardOptions = keyboardOptions
    ) { innerTextField ->
        Row(
            modifier = modifier
                .background(
                    color = White,
                    shape = Shapes.large
                )
                .border(
                    width = 1.dp,
                    color = LineDBDBDB,
                    shape = Shapes.large
                )
                .padding(
                    all = 14.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = Typography.body2
            )

            Spacer(modifier = Modifier.width(10.dp))

            innerTextField()
        }
    }
}

@Composable
fun USearchTextField(
    modifier: Modifier = Modifier,
    msgContent: String,
    hint: String = "",
    readOnly: Boolean = false,
    textStyle: TextStyle = TextStyle(
        fontFamily = Pretendard,
        color = Font191919,
        fontSize = 14.sp
    ),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    onValueChange: (String) -> Unit = { },
) {
    val focusRequester = remember { FocusRequester() }
    var hintMsg by rememberSaveable { mutableStateOf(hint) }

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
                        color = BgF1F1F5,
                        shape = Shapes.large
                    )
                    .padding(
                        top = 9.dp, bottom = 9.dp, start = 12.dp, end = 12.dp
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
                    style = Typography.body3,
                    color = Font70747E
                )

                innerTextField()
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UDateField(
    modifier: Modifier = Modifier,
    hint: String = "",
    calendarBtnOnClick: () -> Unit,
    selectedDate: LocalDate? = null
) {
    Surface(
        modifier = modifier,
        shape = Shapes.large,
        border = BorderStroke(width = 1.dp, color = LineDBDBDB),
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 12.dp, start = 15.dp, end = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val text = selectedDate?.getFormattedTime() ?: hint
            val color = if (selectedDate == null) FontDBDBDB else Font191919

            Text(text = text, style = Typography.body3, color = color)

            CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                IconButton(onClick = calendarBtnOnClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_calendar),
                        tint = Color.Unspecified,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewUTextField() {
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
            hint = stringResource(id = R.string.search_company_hint),
            onValueChange = { }
        )
    }
}

@Preview
@Composable
fun PreviewUDateField() {
    UmuljeongTheme {
        UDateField(
            modifier = Modifier.width(335.dp),
            selectedDate = LocalDate.now(),
            calendarBtnOnClick = { },
            hint = "시작 일자를 선택하세요"
        )
    }
}
