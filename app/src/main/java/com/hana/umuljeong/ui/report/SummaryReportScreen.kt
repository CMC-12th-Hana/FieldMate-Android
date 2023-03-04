package com.hana.umuljeong.ui.report

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hana.umuljeong.R
import com.hana.umuljeong.UmuljeongScreen
import com.hana.umuljeong.data.remote.datasource.fakeCategorySelectionData
import com.hana.umuljeong.data.remote.datasource.fakeReportDataSource
import com.hana.umuljeong.domain.model.ReportEntity
import com.hana.umuljeong.toLocalDate
import com.hana.umuljeong.ui.component.UAppBarWithExitBtn
import com.hana.umuljeong.ui.component.UDatePicker
import com.hana.umuljeong.ui.component.UDropDownMenu
import com.hana.umuljeong.ui.setting.CategoryTag
import com.hana.umuljeong.ui.theme.*
import java.time.LocalDate

@Composable
fun SummaryReportScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    var selectedDate: LocalDate? by rememberSaveable { mutableStateOf(null) }

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
                .fillMaxSize()
                .background(BgF8F8FA)
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

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        elevation = 12.dp
                    ) {
                        UDatePicker(
                            modifier = Modifier.padding(20.dp),
                            selectedDate = selectedDate,
                            eventList = listOf(
                                LocalDate.now(),
                                "2023-02-02".toLocalDate(),
                                "2023-02-11".toLocalDate(),
                                "2023-02-19".toLocalDate(),
                                "2023-02-13".toLocalDate(),
                            ),
                            onDayClicked = { selectedDate = it }
                        )
                    }

                    Spacer(modifier = Modifier.height(30.dp))
                }


                items(fakeReportDataSource) {
                    ExpandableReportItem(
                        navController = navController,
                        memberName = "동쳔",
                        reportEntityList = fakeReportDataSource
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
                        Icon(
                            modifier = Modifier.size(28.dp),
                            painter = painterResource(id = R.drawable.ic_member_profile),
                            tint = Color.Unspecified,
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
                                navController.navigate("${UmuljeongScreen.DetailReport.name}/${report.id}")
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
                                    text = report.name,
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

