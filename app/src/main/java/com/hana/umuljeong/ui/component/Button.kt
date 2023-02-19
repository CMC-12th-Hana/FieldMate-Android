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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hana.umuljeong.R
import com.hana.umuljeong.ui.theme.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle = Typography.body1,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = Shapes.large,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        backgroundColor = Main356DF8,
        contentColor = Color.White,
        disabledBackgroundColor = LineDBDBDB,
        disabledContentColor = Color.White
    ),
    border: BorderStroke = BorderStroke(width = 0.dp, color = Color.Unspecified),
    contentPadding: PaddingValues = PaddingValues(all = 16.dp)
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

        Row(
            Modifier.padding(contentPadding),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = textStyle,
                color = contentColor
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
    shape: Shape = Shapes.large,
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
        border = if (isPressed) BorderStroke(1.dp, Main356DF8) else BorderStroke(
            1.dp,
            LineDBDBDB
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
                style = Typography.title2,
                color = if (isPressed) Main356DF8 else Color.Black
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = description,
                style = Typography.body4,
                color = Font70747E
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
    icon: Painter = painterResource(id = R.drawable.ic_add),
    topBottomPadding: Dp = 16.dp,
    shape: Shape = Shapes.large,
    border: BorderStroke = BorderStroke(1.dp, Line191919),
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
            modifier = Modifier.padding(top = topBottomPadding, bottom = topBottomPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = icon,
                    tint = Color.Unspecified,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = text,
                    style = Typography.body3
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun URoundedArrowButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String,
    icon: Painter,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = Shapes.large,
    border: BorderStroke = BorderStroke(0.dp, Color.Unspecified),
    colors: ButtonColors = ButtonDefaults.buttonColors(
        backgroundColor = BgF8F8FA,
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
        Row(
            modifier = Modifier.padding(top = 15.dp, bottom = 15.dp, start = 20.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = icon,
                    tint = Color.Unspecified,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = text,
                    style = Typography.body2
                )
            }

            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_right),
                tint = Color.Unspecified,
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
fun PreviewUButton() {
    UmuljeongTheme {
        UButton(
            modifier = Modifier.width(335.dp),
            text = "버튼",
            onClick = { }
        )
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
            title = stringResource(id = R.string.add_my_company),
            description = stringResource(id = R.string.add_my_company_info_one),
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