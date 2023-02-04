package com.hana.umuljeong.ui.component

import android.media.Image
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hana.umuljeong.ui.theme.ButtonSkyBlue
import com.hana.umuljeong.ui.theme.FontBlack
import com.hana.umuljeong.ui.theme.FontDarkGray
import com.hana.umuljeong.ui.theme.LineLightGray

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = MaterialTheme.shapes.small,
    border: BorderStroke = BorderStroke(1.dp, ButtonSkyBlue),
    colors: ButtonColors = ButtonDefaults.buttonColors(
        backgroundColor = ButtonSkyBlue,
        contentColor = Color.White,
        disabledBackgroundColor = LineLightGray
    ),
    contentPadding: PaddingValues = PaddingValues(all = 16.dp),
    content: @Composable RowScope.() -> Unit
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
    shape: Shape = MaterialTheme.shapes.medium,
    border: BorderStroke = BorderStroke(1.dp, LineLightGray),
    colors: ButtonColors = ButtonDefaults.buttonColors(
        backgroundColor = Color.White,
    ),
    @StringRes title: Int,
    @StringRes description: Int,
    @DrawableRes image: Int
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
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = imageModifier,
                painter = painterResource(id = image),
                contentDescription = stringResource(id = description)
            )

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = stringResource(id = title),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = FontBlack
                )
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = stringResource(id = description),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = FontDarkGray
                )
            )
        }
    }
}