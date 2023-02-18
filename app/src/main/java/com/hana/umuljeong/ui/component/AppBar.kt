package com.hana.umuljeong.ui.component

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
import com.hana.umuljeong.R
import com.hana.umuljeong.ui.theme.*
import java.time.LocalDate

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UAppBarWithBackBtn(
    modifier: Modifier = Modifier,
    title: String,
    backBtnOnClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(width = 1.dp, color = LineDBDBDB)
    ) {
        Box(
            modifier = Modifier.padding(top = 15.dp, bottom = 15.dp, start = 20.dp, end = 20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                    IconButton(onClick = backBtnOnClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            tint = Color.Unspecified,
                            contentDescription = null
                        )
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    style = Typography.body2,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    text = title
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UAppBarWithEditBtn(
    modifier: Modifier = Modifier,
    title: String,
    backBtnOnClick: () -> Unit,
    editBtnOnClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(width = 1.dp, color = LineDBDBDB)
    ) {
        Column(
            modifier = Modifier.padding(top = 15.dp, bottom = 15.dp, start = 20.dp, end = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                    IconButton(onClick = backBtnOnClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            tint = Color.Unspecified,
                            contentDescription = null
                        )
                    }

                    Text(
                        style = Typography.body2,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        text = title
                    )

                    IconButton(onClick = { editBtnOnClick() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_edit),
                            tint = Color.Unspecified,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UAppBarWithDeleteBtn(
    modifier: Modifier = Modifier,
    title: String,
    backBtnOnClick: () -> Unit,
    deleteBtnOnClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(width = 1.dp, color = LineDBDBDB)
    ) {
        Column(
            modifier = Modifier.padding(top = 15.dp, bottom = 15.dp, start = 20.dp, end = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                    IconButton(onClick = backBtnOnClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            tint = Color.Unspecified,
                            contentDescription = null
                        )
                    }

                    Text(
                        style = Typography.body2,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        text = title
                    )

                    IconButton(onClick = { deleteBtnOnClick() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_delete),
                            tint = Color.Unspecified,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UAppBarWithExitBtn(
    modifier: Modifier = Modifier,
    title: String,
    exitBtnOnClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(width = 1.dp, color = LineDBDBDB)
    ) {
        Box(
            modifier = Modifier.padding(top = 15.dp, bottom = 15.dp, start = 20.dp, end = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                    IconButton(onClick = exitBtnOnClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_exit),
                            tint = Color.Unspecified,
                            contentDescription = null
                        )
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    style = Typography.body2,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    text = title
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UAppBarWithEditAndDeleteBtn(
    modifier: Modifier = Modifier,
    title: String,
    backBtnOnClick: () -> Unit,
    editBtnOnClick: () -> Unit,
    deleteBtnOnClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(width = 1.dp, color = LineDBDBDB)
    ) {
        Box(
            modifier = Modifier.padding(top = 15.dp, bottom = 15.dp, start = 20.dp, end = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    style = Typography.body2,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    text = title
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                    IconButton(onClick = backBtnOnClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            tint = Color.Unspecified,
                            contentDescription = null
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        IconButton(onClick = { editBtnOnClick() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_edit),
                                tint = Color.Unspecified,
                                contentDescription = null
                            )
                        }

                        IconButton(onClick = { deleteBtnOnClick() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_delete),
                                tint = Color.Unspecified,
                                contentDescription = null
                            )
                        }
                    }
                }
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
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Column(
            modifier = modifier
                .padding(top = 15.dp, bottom = 15.dp, start = 20.dp, end = 20.dp)
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
                            style = Typography.body2
                        )

                        Spacer(modifier = Modifier.width(5.dp))

                        IconButton(
                            onClick = expandBtnOnClick,
                            modifier = Modifier.height(15.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_expand_more),
                                tint = Color.Unspecified,
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
                .background(BgF1F1F5)
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
            title = stringResource(id = R.string.home),
            backBtnOnClick = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUAppBarWithEditBtn() {
    UmuljeongTheme {
        UAppBarWithEditBtn(
            title = stringResource(id = R.string.home),
            backBtnOnClick = { },
            editBtnOnClick = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUAppBarWithDeleteBtn() {
    UmuljeongTheme {
        UAppBarWithDeleteBtn(
            title = stringResource(id = R.string.home),
            backBtnOnClick = { },
            deleteBtnOnClick = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUAppBarWithEditAndDeleteBtn() {
    UmuljeongTheme {
        UAppBarWithEditAndDeleteBtn(
            title = stringResource(id = R.string.home),
            backBtnOnClick = { },
            editBtnOnClick = { },
            deleteBtnOnClick = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUAppBarWithExitBtn() {
    UmuljeongTheme {
        UAppBarWithExitBtn(
            title = stringResource(id = R.string.home),
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