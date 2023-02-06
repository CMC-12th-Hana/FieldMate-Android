package com.hana.umuljeong.ui.component

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import java.time.LocalDate

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

@Composable
fun UAppBarWithEditBtn(
    modifier: Modifier = Modifier,
    @StringRes title: Int,
    editId: Long,
    backBtnOnClick: () -> Unit,
    editBtnOnClick: (Long) -> Unit
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

            IconButton(onClick = { editBtnOnClick(editId) }) {
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UHomeAppBar(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate,
    onDayClicked: (LocalDate) -> Unit,
    expandBtnOnClick: () -> Unit,
    settingBtnOnClick: () -> Unit,
    alarmBtnOnClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = modifier
                .padding(top = 20.dp, bottom = 20.dp, start = 20.dp, end = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                        IconButton(
                            onClick = settingBtnOnClick
                        ) {
                            Icon(
                                painter = painterResource(
                                    id = R.drawable.ic_setting
                                ),
                                tint = Color.Unspecified,
                                contentDescription = null
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Text(
                            text = "${selectedDate.year}년 ${selectedDate.monthValue}월",
                            fontSize = 16.sp
                        )

                        Spacer(modifier = Modifier.width(5.dp))

                        IconButton(
                            onClick = expandBtnOnClick,
                            modifier = Modifier.height(15.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_expand_more),
                                contentDescription = null
                            )
                        }
                    }
                }

                CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                    IconButton(
                        onClick = alarmBtnOnClick
                    ) {
                        Icon(
                            painter = painterResource(
                                id = R.drawable.ic_alarm_on
                            ),
                            tint = Color.Unspecified,
                            contentDescription = null
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            UHorizontalCalendar(
                modifier = modifier
                    .padding(top = 10.dp, bottom = 10.dp, start = 10.dp, end = 10.dp),
                selectedDate = selectedDate,
                onDayClicked = onDayClicked
            )
        }

        Spacer(
            modifier = Modifier
                .background(color = Color(0xFFF6F6F6))
                .fillMaxWidth()
                .height(8.dp)
        )
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
            editId = 0L,
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

@Preview(showBackground = true)
@Composable
fun PreviewUHomeAppBar() {
    UmuljeongTheme {
        UHomeAppBar(
            selectedDate = LocalDate.now(),
            onDayClicked = { },
            expandBtnOnClick = { },
            settingBtnOnClick = { },
            alarmBtnOnClick = { }
        )
    }
}