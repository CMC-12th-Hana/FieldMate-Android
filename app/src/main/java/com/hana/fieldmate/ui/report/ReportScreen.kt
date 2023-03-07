package com.hana.fieldmate.ui.report

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hana.fieldmate.FieldMateScreen
import com.hana.fieldmate.R
import com.hana.fieldmate.data.local.fakeCategorySelectionData
import com.hana.fieldmate.data.local.fakeReportDataSource
import com.hana.fieldmate.domain.model.ReportEntity
import com.hana.fieldmate.ui.component.*
import com.hana.fieldmate.ui.setting.CategoryTag
import com.hana.fieldmate.ui.theme.*
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ReportScreen(
    modifier: Modifier = Modifier,
    uiState: ReportListUiState,
    navController: NavController,
    addBtnOnClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded },
        skipHalfExpanded = true
    )
    var selectedDate by rememberSaveable { mutableStateOf(LocalDate.now()) }
    var showMemberReportSwitch by rememberSaveable { mutableStateOf(false) }

    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetShape = RoundedCornerShape(
            topStart = 12.dp,
            topEnd = 12.dp
        ),
        sheetBackgroundColor = Color.White,
        sheetContent = {
            Column(modifier = Modifier.fillMaxWidth()) {
                DatePicker(
                    modifier = Modifier.padding(40.dp),
                    selectedDate = selectedDate,
                    onDayClicked = { selectedDate = it }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                FHomeAppBar(
                    selectedDate = selectedDate,
                    onDayClicked = {
                        selectedDate = it
                        coroutineScope.launch {
                            modalSheetState.animateTo(ModalBottomSheetValue.Hidden)
                        }
                    },
                    expandBtnOnClick = {
                        coroutineScope.launch {
                            modalSheetState.animateTo(ModalBottomSheetValue.Expanded)
                        }
                    },
                    settingBtnOnClick = {
                        navController.navigate(FieldMateScreen.Setting.name)
                    },
                    alarmBtnOnClick = { }
                )
            },
            bottomBar = {
                FBottomBar(
                    navController = navController
                )
            },
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(BgF8F8FA)
            ) {
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(start = 20.dp, end = 20.dp)
                        .background(color = BgF8F8FA),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    item {
                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = stringResource(id = R.string.show_member_report),
                                style = Typography.body3,
                                color = Font70747E
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            FSwitch(
                                switchOn = showMemberReportSwitch,
                                switchOnClick = { showMemberReportSwitch = it }
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    item {
                        FAddButton(
                            onClick = addBtnOnClick,
                            text = stringResource(id = R.string.add_report),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    items(uiState.reportEntityList) { report ->
                        if (showMemberReportSwitch) {
                            ExpandableReportItem(
                                navController = navController,
                                memberName = "동쳔",
                                reportEntityList = fakeReportDataSource
                            )
                        } else {
                            ReportItem(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    navController.navigate("${FieldMateScreen.DetailReport.name}/${report.id}")
                                },
                                reportEntity = report
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ReportItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    shape: Shape = Shapes.large,
    reportEntity: ReportEntity
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        color = Color.White,
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = reportEntity.title,
                style = Typography.body2
            )

            val categoryColor =
                CategoryColor[fakeCategorySelectionData.indexOf(reportEntity.category)]

            CategoryTag(text = reportEntity.category, color = categoryColor)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExpandableReportItem(
    modifier: Modifier = Modifier,
    navController: NavController,
    memberName: String,
    reportEntityList: List<ReportEntity>,
    shape: Shape = Shapes.large
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    Surface(
        modifier = modifier,
        shape = shape,
        color = Color.White,
        elevation = 0.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                onClick = { isExpanded = !isExpanded },
                modifier = modifier,
                shape = shape,
                color = Color.White,
                elevation = 0.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(R.drawable.ic_member_profile)
                                .build(),
                            modifier = Modifier
                                .size(40.dp)
                                .aspectRatio(1.0F),
                            filterQuality = FilterQuality.Low,
                            contentScale = ContentScale.Crop,
                            contentDescription = null
                        )

                        Text(
                            text = memberName,
                            style = Typography.body2
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        if (!isExpanded) Text(
                            text = "${reportEntityList.size}",
                            style = Typography.body1,
                            color = Main356DF8
                        )

                        Icon(
                            painter = painterResource(
                                id = if (!isExpanded) R.drawable.ic_expand_more else R.drawable.ic_expand_less
                            ),
                            tint = Color.Unspecified,
                            contentDescription = null
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = isExpanded,
            ) {
                Column {
                    for (report in reportEntityList) {
                        Surface(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(start = 4.dp, end = 4.dp),
                            shape = Shapes.medium,
                            onClick = {
                                navController.navigate("${FieldMateScreen.DetailReport.name}/${report.id}")
                            },
                            color = Color.White,
                            elevation = 0.dp
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = 15.dp,
                                        bottom = 15.dp,
                                        start = 20.dp,
                                        end = 15.dp
                                    ),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = report.title,
                                    style = Typography.body2
                                )

                                val categoryColor =
                                    CategoryColor[fakeCategorySelectionData.indexOf(report.category)]

                                CategoryTag(text = report.category, color = categoryColor)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(15.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    FieldMateTheme {
        ReportScreen(
            uiState = ReportListUiState(),
            navController = rememberNavController(),
            addBtnOnClick = { }
        )
    }
}