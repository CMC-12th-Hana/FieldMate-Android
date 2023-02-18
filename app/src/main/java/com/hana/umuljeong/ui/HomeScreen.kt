package com.hana.umuljeong.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hana.umuljeong.R
import com.hana.umuljeong.UmuljeongScreen
import com.hana.umuljeong.data.datasource.fakeCategorySelectionData
import com.hana.umuljeong.data.model.Report
import com.hana.umuljeong.ui.component.*
import com.hana.umuljeong.ui.report.ReportListUiState
import com.hana.umuljeong.ui.theme.*
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: ReportListUiState,
    navController: NavController,
    addBtnOnClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded },
        skipHalfExpanded = true,
    )
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetShape = RoundedCornerShape(
            topStart = 12.dp,
            topEnd = 12.dp
        ),
        sheetBackgroundColor = Color.White,
        sheetContent = {
            Column(modifier = Modifier.fillMaxWidth()) {
                UDatePicker(
                    selectedDate = selectedDate,
                    onDayClicked = { selectedDate = it }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                UHomeAppBar(
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
                        navController.navigate(UmuljeongScreen.Setting.name)
                    },
                    alarmBtnOnClick = { }
                )
            },
            bottomBar = {
                UBottomBar(
                    navController = navController
                )
            },
        ) { innerPadding ->
            HomeContent(
                modifier = Modifier.padding(innerPadding),
                reportList = uiState.reportList,
                navController = navController,
                addBtnOnClick = addBtnOnClick
            )
        }
    }
}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    reportList: List<Report>,
    navController: NavController,
    addBtnOnClick: () -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(15.dp))

            UAddButton(
                onClick = addBtnOnClick,
                text = stringResource(id = R.string.add_report),
                modifier = Modifier.width(335.dp)
            )
        }

        items(reportList) { report ->
            ReportItem(
                modifier = Modifier.width(335.dp),
                onClick = {
                    navController.navigate("${UmuljeongScreen.DetailReport.name}/${report.id}")
                },
                report = report
            )
        }

        item {
            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ReportItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    shape: Shape = Shapes.large,
    report: Report
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        color = BgF8F8FA,
        elevation = 0.dp
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = report.name,
                        style = Typography.body1
                    )
                    Text(
                        text = report.customer,
                        style = Typography.body3,
                        color = Font70747E
                    )
                }

                val categoryColor =
                    CategoryColor[fakeCategorySelectionData.indexOf(report.category)]

                Surface(
                    shape = Shapes.large,
                    color = Color.Transparent,
                    border = BorderStroke(width = 1.dp, color = categoryColor.first),
                    contentColor = Color.White,
                    elevation = 0.dp
                ) {
                    Text(
                        modifier = Modifier.padding(
                            top = 6.dp, bottom = 6.dp, start = 10.dp, end = 10.dp
                        ),
                        text = report.category,
                        style = Typography.body3,
                        color = categoryColor.second
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    UmuljeongTheme {
        HomeScreen(
            uiState = ReportListUiState(),
            navController = rememberNavController(),
            addBtnOnClick = { }
        )
    }
}