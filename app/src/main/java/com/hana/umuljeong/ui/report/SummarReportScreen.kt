package com.hana.umuljeong.ui.report

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hana.umuljeong.R
import com.hana.umuljeong.UmuljeongScreen
import com.hana.umuljeong.data.datasource.fakeCategorySelectionData
import com.hana.umuljeong.data.datasource.fakeReportData
import com.hana.umuljeong.ui.ReportItem
import com.hana.umuljeong.ui.component.UAppBarWithExitBtn
import com.hana.umuljeong.ui.component.UDatePicker
import com.hana.umuljeong.ui.component.UDropDownMenu
import java.time.LocalDate

@Composable
fun SummaryReportScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Scaffold(
        topBar = {
            UAppBarWithExitBtn(
                title = stringResource(id = R.string.report_summary),
                exitBtnOnClick = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(30.dp))

                    var selectedCategory by rememberSaveable { mutableStateOf("전체") }

                    UDropDownMenu(
                        modifier = Modifier.fillMaxWidth(),
                        options = fakeCategorySelectionData,
                        selectedOption = selectedCategory,
                        optionOnClick = { selectedCategory = it }
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        elevation = 12.dp
                    ) {
                        UDatePicker(
                            modifier = Modifier.padding(20.dp),
                            selectedDate = LocalDate.now(),
                            onDayClicked = { }
                        )
                    }

                    Spacer(modifier = Modifier.height(30.dp))
                }


                items(fakeReportData) { report ->
                    ReportItem(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            navController.navigate("${UmuljeongScreen.DetailReport.name}/${report.id}")
                        },
                        report = report
                    )

                    Spacer(modifier = Modifier.height(15.dp))
                }

                item {
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
    }
}

