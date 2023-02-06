package com.hana.umuljeong.ui

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
import com.hana.umuljeong.data.datasource.fakeReportData
import com.hana.umuljeong.data.model.Report
import com.hana.umuljeong.ui.component.*
import com.hana.umuljeong.ui.theme.*
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
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
                DatePicker(
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
                    onDayClicked = { selectedDate = it },
                    expandBtnOnClick = {
                        coroutineScope.launch {
                            modalSheetState.animateTo(ModalBottomSheetValue.Expanded)
                        }
                    },
                    settingBtnOnClick = { },
                    alarmBtnOnClick = { }
                )
            },
            bottomBar = {
                UBottomBar(
                    navController = navController
                )
            },
        ) { innerPadding ->
            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(innerPadding),
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

                items(fakeReportData) { report ->
                    ReportItem(
                        onClick = {
                            navController.navigate("${UmuljeongScreen.DetailReport.name}/${report.id}")
                        },
                        modifier = Modifier.width(335.dp),
                        report = report
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(15.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ReportItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = Shapes.medium,
    report: Report
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        color = BgLightGray,
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
                Text(text = report.name)

                Surface(
                    shape = Shapes.medium,
                    color = ButtonSkyBlue,
                    contentColor = Color.White,
                    elevation = 0.dp
                ) {
                    Text(
                        modifier = Modifier.padding(
                            top = 6.dp, bottom = 6.dp, start = 10.dp, end = 10.dp
                        ),
                        text = report.category
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
        HomeScreen(navController = rememberNavController(), addBtnOnClick = { })
    }
}