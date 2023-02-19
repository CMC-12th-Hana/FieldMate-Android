package com.hana.umuljeong.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hana.umuljeong.R
import com.hana.umuljeong.data.datasource.fakeCategorySelectionData
import com.hana.umuljeong.ui.theme.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UDropDownMenu(
    modifier: Modifier = Modifier,
    title: String = "",
    options: List<String>,
    optionOnClick: (String) -> Unit,
    selectedOption: String,
    shape: Shape = Shapes.large,
    border: BorderStroke = BorderStroke(1.dp, LineDBDBDB)
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    Box {
        Column {
            Surface(
                modifier = modifier,
                color = Color.White,
                elevation = 0.dp,
                shape = shape,
                onClick = { isExpanded = !isExpanded },
                border = border
            ) {
                Row(
                    modifier = modifier.padding(
                        all = 14.dp
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        style = Typography.body2
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = selectedOption,
                            style = Typography.body3,
                            color = Font70747E,
                        )

                        Icon(
                            painter = painterResource(
                                id = if (isExpanded) R.drawable.ic_expand_less else R.drawable.ic_expand_more
                            ),
                            contentDescription = null
                        )
                    }

                }
            }

            DropdownMenu(
                modifier = modifier.fillMaxWidth(),
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false },
            ) {
                options.forEach { option ->
                    var fontWeight by remember {
                        mutableStateOf(FontWeight.Medium)
                    }
                    var fontColor by remember {
                        mutableStateOf(Font70747E)
                    }

                    DropdownMenuItem(
                        modifier = Modifier
                            .pointerInput(Unit) {
                                detectTapGestures {
                                    fontWeight = FontWeight.Bold
                                    fontColor = Font191919
                                }
                            },
                        onClick = {
                            optionOnClick(option)
                            isExpanded = false
                        },
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(
                                    top = 15.dp,
                                    bottom = 15.dp,
                                    start = 30.dp,
                                    end = 30.dp
                                ),
                            text = option,
                            style = TextStyle(
                                fontFamily = Pretendard,
                                color = fontColor,
                                fontWeight = fontWeight,
                                fontSize = 16.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewDropDownMenuWithTitle() {
    UmuljeongTheme {
        UDropDownMenu(
            modifier = Modifier.width(335.dp),
            title = stringResource(id = R.string.business_name),
            options = fakeCategorySelectionData,
            selectedOption = "",
            optionOnClick = { }
        )
    }
}