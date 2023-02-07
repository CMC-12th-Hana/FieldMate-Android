package com.hana.umuljeong.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hana.umuljeong.R
import com.hana.umuljeong.ui.theme.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = Shapes.small,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        backgroundColor = ButtonSkyBlue,
        contentColor = Color.White,
        disabledBackgroundColor = LineLightGray,
        disabledContentColor = Color.White
    ),
    border: BorderStroke = BorderStroke(width = 0.dp, color = Color.Unspecified),
    contentPadding: PaddingValues = PaddingValues(all = 16.dp),
    content: @Composable RowScope.() -> Unit
) {
    val contentColor by colors.contentColor(enabled)
    Surface(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        border = border,
        color = colors.backgroundColor(enabled).value,
        contentColor = contentColor.copy(alpha = 1f),
        elevation = 0.dp,
        interactionSource = interactionSource,
    ) {
        ProvideTextStyle(
            value = MaterialTheme.typography.button
        ) {
            Row(
                Modifier
                    .defaultMinSize(
                        minWidth = ButtonDefaults.MinWidth,
                        minHeight = ButtonDefaults.MinHeight
                    )
                    .padding(contentPadding),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                content = content
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UImageButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    imageModifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = Shapes.medium,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        backgroundColor = Color.White,
    ),
    title: String,
    description: String,
    @DrawableRes image: Int
) {
    val isPressed by interactionSource.collectIsPressedAsState()

    val contentColor by colors.contentColor(enabled)
    Surface(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        color = colors.backgroundColor(enabled).value,
        contentColor = contentColor.copy(alpha = 1f),
        border = if (isPressed) BorderStroke(1.dp, ButtonSkyBlue) else BorderStroke(
            1.dp,
            LineLightGray
        ),
        elevation = 0.dp,
        interactionSource = interactionSource,
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = imageModifier,
                painter = painterResource(id = image),
                contentDescription = description
            )

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = title,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isPressed) ButtonSkyBlue else Color.Black
                )
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = description,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = FontDarkGray
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UAddButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = Shapes.medium,
    border: BorderStroke = BorderStroke(1.dp, LineLightGray),
    colors: ButtonColors = ButtonDefaults.buttonColors(
        backgroundColor = Color.White,
        contentColor = Color.Black
    )
) {
    val contentColor by colors.contentColor(enabled)
    Surface(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        color = colors.backgroundColor(enabled).value,
        contentColor = contentColor.copy(alpha = 1f),
        border = border,
        elevation = 0.dp,
        interactionSource = interactionSource,
    ) {
        Column(
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    tint = Color.Unspecified,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = text,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewUButton() {
    UmuljeongTheme {
        UButton(
            modifier = Modifier.width(335.dp),
            onClick = { }
        ) {
            Text(
                text = "버튼"
            )
        }
    }
}

@Preview
@Composable
fun PreviewUImageButton() {
    UmuljeongTheme {
        UImageButton(
            modifier = Modifier.size(width = 335.dp, height = 230.dp),
            imageModifier = Modifier.size(width = 110.dp, height = 100.dp),
            onClick = { },
            title = stringResource(id = R.string.add_company),
            description = stringResource(id = R.string.add_company_info),
            image = R.drawable.img_add_company
        )
    }
}

@Preview
@Composable
fun PreviewAddButton() {
    UmuljeongTheme {
        UAddButton(
            onClick = { },
            text = stringResource(id = R.string.add_report),
            modifier = Modifier.width(335.dp)
        )
    }
}