package com.hana.umuljeong.ui.setting

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.hana.umuljeong.R
import com.hana.umuljeong.ui.component.UButton
import com.hana.umuljeong.ui.component.UTextField
import com.hana.umuljeong.ui.theme.*

@Composable
fun AddCategoryDialog(
    modifier: Modifier = Modifier,
    addBtnOnClick: () -> Unit,
    cancelBtnOnClick: () -> Unit
) {
    Dialog(
        onDismissRequest = { }
    ) {
        var category by rememberSaveable { mutableStateOf("") }
        var selectedColorIdx by rememberSaveable { mutableStateOf(-1) }

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color.White
        ) {
            Column(
                modifier = modifier.padding(
                    top = 30.dp,
                    bottom = 40.dp,
                    start = 20.dp,
                    end = 20.dp
                ),
            ) {
                Text(text = stringResource(id = R.string.category_input), style = Typography.body3)

                Spacer(modifier = Modifier.height(8.dp))

                UTextField(
                    modifier = Modifier.fillMaxWidth(),
                    msgContent = category,
                    hint = stringResource(id = R.string.category_input_hint),
                    onValueChange = { category = it }
                )

                Spacer(modifier = Modifier.height(30.dp))

                Text(text = stringResource(id = R.string.category_color), style = Typography.body3)

                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    border = BorderStroke(1.dp, LineDBDBDB),
                    shape = Shapes.large
                ) {
                    Column(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(
                                top = 25.dp,
                                bottom = 25.dp,
                                start = 20.dp,
                                end = 20.dp
                            ),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            for (i in 0..6) {
                                ColorItem(
                                    color = CategoryColor[i],
                                    selected = selectedColorIdx == i,
                                    onClick = { selectedColorIdx = it }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    UButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        onClick = addBtnOnClick,
                        text = stringResource(id = R.string.complete),
                        contentPadding = PaddingValues(top = 14.dp, bottom = 14.dp)
                    )

                    UButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        onClick = cancelBtnOnClick,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = BgF1F1F5,
                            contentColor = Font70747E
                        ),
                        text = stringResource(id = R.string.cancel),
                        contentPadding = PaddingValues(top = 14.dp, bottom = 14.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ColorItem(
    modifier: Modifier = Modifier,
    color: Color,
    selected: Boolean = false,
    onClick: (Int) -> Unit
) {
    Surface(
        modifier = modifier.size(42.dp),
        shape = CircleShape,
        color = color.copy(alpha = 0.4f),
        border = BorderStroke(1.5.dp, if (selected) Line191919 else Color.Transparent),
        onClick = { onClick(CategoryColor.indexOf(color)) }
    ) {

    }
}