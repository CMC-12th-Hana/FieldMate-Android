package com.hana.fieldmate.ui.component

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hana.fieldmate.R
import com.hana.fieldmate.ui.theme.*
import java.time.LocalDate

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FAppBarWithBackBtn(
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
fun FAppBarWithEditBtn(
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
fun FAppBarWithDeleteBtn(
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
fun FAppBarWithExitBtn(
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
fun FAppBarWithEditAndDeleteBtn(
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

                    Row(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
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
fun FHomeAppBar(
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
                .padding(top = 25.dp, bottom = 15.dp, start = 20.dp, end = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                        Spacer(modifier = Modifier.width(20.dp))

                        Text(
                            text = "${selectedDate.year}년 ${selectedDate.monthValue}월",
                            style = Typography.title2,
                            fontWeight = FontWeight.SemiBold
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

                Row(verticalAlignment = Alignment.CenterVertically) {
                    CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                        IconButton(
                            onClick = settingBtnOnClick
                        ) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(
                                    id = R.drawable.ic_setting
                                ),
                                tint = Color.Unspecified,
                                contentDescription = null
                            )
                        }

                        Spacer(modifier = Modifier.width(15.dp))

                        IconButton(
                            onClick = alarmBtnOnClick
                        ) {
                            Icon(
                                painter = painterResource(
                                    id = R.drawable.ic_alarm
                                ),
                                tint = Color.Unspecified,
                                contentDescription = null
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            HorizontalCalendar(
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
fun PreviewFAppBarWithBackBtn() {
    FieldMateTheme {
        FAppBarWithBackBtn(
            title = stringResource(id = R.string.report),
            backBtnOnClick = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFAppBarWithEditBtn() {
    FieldMateTheme {
        FAppBarWithEditBtn(
            title = stringResource(id = R.string.report),
            backBtnOnClick = { },
            editBtnOnClick = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFAppBarWithDeleteBtn() {
    FieldMateTheme {
        FAppBarWithDeleteBtn(
            title = stringResource(id = R.string.report),
            backBtnOnClick = { },
            deleteBtnOnClick = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFAppBarWithEditAndDeleteBtn() {
    FieldMateTheme {
        FAppBarWithEditAndDeleteBtn(
            title = stringResource(id = R.string.report),
            backBtnOnClick = { },
            editBtnOnClick = { },
            deleteBtnOnClick = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFAppBarWithExitBtn() {
    FieldMateTheme {
        FAppBarWithExitBtn(
            title = stringResource(id = R.string.report),
            exitBtnOnClick = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFHomeAppBar() {
    FieldMateTheme {
        FHomeAppBar(
            selectedDate = LocalDate.now(),
            onDayClicked = { },
            expandBtnOnClick = { },
            settingBtnOnClick = { },
            alarmBtnOnClick = { }
        )
    }
}