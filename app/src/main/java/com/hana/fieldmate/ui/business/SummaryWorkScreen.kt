package com.hana.fieldmate.ui.task

import androidx.compose.foundation.background
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
import com.hana.fieldmate.FieldMateScreen
import com.hana.fieldmate.R
import com.hana.fieldmate.data.local.fakeCategorySelectionData
import com.hana.fieldmate.data.local.fakeTaskDataSource
import com.hana.fieldmate.toLocalDate
import com.hana.fieldmate.ui.component.DatePicker
import com.hana.fieldmate.ui.component.FAppBarWithBackBtn
import com.hana.fieldmate.ui.component.FDropDownMenu
import com.hana.fieldmate.ui.theme.BgF8F8FA
import java.time.LocalDate

@Composable
fun SummaryTaskScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    var selectedDate: LocalDate? by rememberSaveable { mutableStateOf(null) }

    Scaffold(
        topBar = {
            FAppBarWithBackBtn(
                title = stringResource(id = R.string.task_by_day),
                backBtnOnClick = { navController.navigateUp() }
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

                    FDropDownMenu(
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
                        DatePicker(
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


                items(fakeTaskDataSource) { task ->
                    TaskItem(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            navController.navigate("${FieldMateScreen.DetailTask.name}/${task.id}")
                        },
                        taskEntity = task
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

