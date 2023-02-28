package com.hana.umuljeong.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
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

    BoxWithConstraints(modifier = modifier) {
        Surface(
            color = Color.White,
            elevation = 0.dp,
            shape = shape,
            onClick = { isExpanded = !isExpanded },
            border = border
        ) {
            Row(
                modifier = Modifier.padding(
                    top = 11.dp,
                    bottom = 11.dp,
                    start = 15.dp,
                    end = 15.dp
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = if (title.isNotEmpty()) Modifier.width(56.dp) else Modifier,
                    text = title,
                    style = Typography.body2
                )

                if (title.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(15.dp))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = selectedOption,
                        style = Typography.body2,
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

            DropdownMenu(
                modifier = Modifier.width(maxWidth),
                expanded = isExpanded,
                offset = DpOffset(0.dp, 6.dp),
                onDismissRequest = { isExpanded = false },
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            optionOnClick(option)
                            isExpanded = false
                        },
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(
                                    top = 10.dp,
                                    bottom = 10.dp,
                                    start = 25.dp,
                                    end = 25.dp
                                ),
                            text = option,
                            style = Typography.body2,
                            color = Font70747E
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
            selectedOption = "사업",
            optionOnClick = { }
        )
    }
}