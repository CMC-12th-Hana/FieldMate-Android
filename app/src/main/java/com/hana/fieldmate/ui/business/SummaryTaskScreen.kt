package com.hana.fieldmate.ui.business

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hana.fieldmate.App
import com.hana.fieldmate.R
import com.hana.fieldmate.data.ErrorType
import com.hana.fieldmate.ui.DialogType
import com.hana.fieldmate.ui.business.viewmodel.BusinessTaskViewModel
import com.hana.fieldmate.ui.component.*
import com.hana.fieldmate.ui.navigation.NavigateActions
import com.hana.fieldmate.ui.task.TaskItem
import com.hana.fieldmate.ui.theme.BgF8F8FA
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun SummaryTaskScreen(
    modifier: Modifier = Modifier,
    viewModel: BusinessTaskViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val userInfo = App.getInstance().getUserInfo()
    val taskDateList = uiState.taskDateList
    val taskList = uiState.taskList
    val categoryEntityList = uiState.categoryList

    when (uiState.dialog) {
        is DialogType.Error -> {
            when (val error = (uiState.dialog as DialogType.Error).errorType) {
                is ErrorType.JwtExpired -> {
                    BackToLoginDialog(onClose = { viewModel.backToLogin() })
                }
                is ErrorType.General -> {
                    ErrorDialog(
                        errorMessage = error.errorMessage,
                        onClose = { viewModel.onDialogClosed() }
                    )
                }
            }
        }
        else -> {}
    }

    var selectedYearMonth: YearMonth by rememberSaveable { mutableStateOf(YearMonth.from(LocalDate.now())) }
    var selectedDate: LocalDate? by remember { mutableStateOf(null) }
    var selectedCategory by remember { mutableStateOf("전체") }
    var selectedCategoryId: Long? by remember { mutableStateOf(null) }

    LaunchedEffect(selectedYearMonth, selectedCategoryId) {
        val year = selectedYearMonth.year
        val month = selectedYearMonth.month

        viewModel.loadTaskDateList(year, month.value, selectedCategoryId)
        viewModel.loadTaskListByDate(year, month.value, null, selectedCategoryId)
    }

    LaunchedEffect(selectedDate, selectedCategoryId) {
        val year = selectedDate?.year ?: LocalDate.now().year
        val month = selectedDate?.month ?: LocalDate.now().month
        val day = selectedDate?.dayOfMonth

        viewModel.loadCategories(userInfo.companyId)
        viewModel.loadTaskListByDate(year, month.value, day, selectedCategoryId)
    }

    Scaffold(
        topBar = {
            FAppBarWithBackBtn(
                title = stringResource(id = R.string.task_by_day),
                backBtnOnClick = {
                    viewModel.navigateTo(NavigateActions.navigateUp())
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(BgF8F8FA)
                .padding(innerPadding)
        ) {
            LoadingContent(loadingState = uiState.taskListLoadingState) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(30.dp))

                        val categoryList = mutableListOf("전체")
                        categoryList.addAll(categoryEntityList.map { it.name })

                        FDropDownMenu(
                            modifier = Modifier.fillMaxWidth(),
                            options = categoryList,
                            selectedOption = selectedCategory,
                            optionOnClick = {
                                selectedCategory = it
                                selectedCategoryId =
                                    categoryEntityList.find { category -> category.name == selectedCategory }?.id
                            }
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
                                eventList = taskDateList,
                                currentYearMonth = selectedYearMonth,
                                onDayClicked = { selectedDate = it },
                                onYearMonthChanged = { selectedYearMonth = it }
                            )
                        }

                        Spacer(modifier = Modifier.height(30.dp))
                    }


                    items(taskList) { task ->
                        TaskItem(
                            modifier = Modifier.fillMaxWidth(),
                            showAuthor = true,
                            onClick = {
                                viewModel.navigateTo(
                                    NavigateActions.SummaryTaskScreen
                                        .toDetailTaskScreen(task.id)
                                )
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
}

