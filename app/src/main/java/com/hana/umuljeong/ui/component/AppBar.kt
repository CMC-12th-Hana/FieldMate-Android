package com.hana.umuljeong.ui.component

import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hana.umuljeong.R
import com.hana.umuljeong.ui.theme.LineLightGray
import com.hana.umuljeong.ui.theme.UmuljeongTheme

enum class AppBarStatus() {
    AppBarWithBackBtn,
    AppBarWithEditBtn,
    AppBarWithExitBtn
}

@Composable
fun UAppBarWithBackBtn(
    modifier: Modifier = Modifier,
    @StringRes title: Int,
    backBtnOnClick: () -> Unit
) {
    TopAppBar(
        backgroundColor = Color.White,
        elevation = 0.dp,
        modifier = modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = LineLightGray)
    ) {
        Box {
            Row(
                modifier = modifier.fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = backBtnOnClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = null
                    )
                }
            }

            Row(Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically) {

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    text = stringResource(id = title)
                )
            }
        }
    }
}

@Composable
fun UAppBarWithEditBtn(
    modifier: Modifier = Modifier,
    @StringRes title: Int,
    backBtnOnClick: () -> Unit,
    editBtnOnClick: () -> Unit
) {
    TopAppBar(
        backgroundColor = Color.White,
        elevation = 0.dp,
        modifier = modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = LineLightGray)
    ) {
        Row(
            modifier = modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = backBtnOnClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = null
                )
            }

            Text(
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                maxLines = 1,
                text = stringResource(id = title)
            )

            IconButton(onClick = editBtnOnClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun UAppBarWithExitBtn(
    modifier: Modifier = Modifier,
    @StringRes title: Int,
    exitBtnOnClick: () -> Unit
) {
    TopAppBar(
        backgroundColor = Color.White,
        elevation = 0.dp,
        modifier = modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = LineLightGray)
    ) {
        Box {
            Row(
                modifier = modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = exitBtnOnClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_exit),
                        contentDescription = null
                    )
                }
            }

            Row(
                Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    text = stringResource(id = title)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUAppBarWithBackBtn() {
    UmuljeongTheme {
        UAppBarWithBackBtn(
            title = R.string.home,
            backBtnOnClick = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUAppBarWithEditBtn() {
    UmuljeongTheme {
        UAppBarWithEditBtn(
            title = R.string.home,
            backBtnOnClick = { },
            editBtnOnClick = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUAppBarWithExitBtn() {
    UmuljeongTheme {
        UAppBarWithExitBtn(
            title = R.string.home,
            exitBtnOnClick = { }
        )
    }
}