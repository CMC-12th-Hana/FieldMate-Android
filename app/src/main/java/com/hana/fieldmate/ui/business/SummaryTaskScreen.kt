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
import androidx.navigation.NavController
import com.hana.fieldmate.FieldMateScreen
import com.hana.fieldmate.R
import com.hana.fieldmate.data.local.UserInfo
import com.hana.fieldmate.toLocalDate
import com.hana.fieldmate.ui.DialogAction
import com.hana.fieldmate.ui.DialogState
import com.hana.fieldmate.ui.Event
import com.hana.fieldmate.ui.component.DatePicker
import com.hana.fieldmate.ui.component.ErrorDialog
import com.hana.fieldmate.ui.component.FAppBarWithBackBtn
import com.hana.fieldmate.ui.component.FDropDownMenu
import com.hana.fieldmate.ui.task.TaskItem
import com.hana.fieldmate.ui.theme.BgF8F8FA
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate

@Composable
fun SummaryTaskScreen(
    modifier: Modifier = Modifier,
    uiState: BusinessTaskUiState,
    userInfo: UserInfo,
    eventsFlow: Flow<Event>,
    sendEvent: (Event) -> Unit,
    loadTaskListByDate: (Int, Int, Int?, Long?) -> Unit,
    loadCategories: (Long) -> Unit,
    navController: NavController
) {
    val taskEntityList = uiState.taskEntityList
    val categoryEntityList = uiState.categoryEntityList

    var selectedDate: LocalDate? by rememberSaveable { mutableStateOf(null) }
    var selectedCategory by rememberSaveable { mutableStateOf("전체") }
    var selectedCategoryId: Long? by rememberSaveable { mutableStateOf(null) }

    var errorDialogOpen by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf("") }

    if (errorDialogOpen) ErrorDialog(
        errorMessage = errorMessage,
        onClose = { sendEvent(Event.Dialog(DialogState.Error, DialogAction.Close)) }
    )

    LaunchedEffect(selectedDate, selectedCategoryId) {
        val year = selectedDate?.year ?: LocalDate.now().year
        val month = selectedDate?.month ?: LocalDate.now().month
        val day = selectedDate?.dayOfMonth

        loadCategories(userInfo.companyId)
        loadTaskListByDate(year, month.value, day, selectedCategoryId)

        eventsFlow.collectLatest { event ->
            when (event) {
                is Event.NavigateTo -> navController.navigate(event.destination)
                is Event.NavigatePopUpTo -> navController.navigate(event.destination) {
                    popUpTo(event.popUpDestination) {
                        inclusive = event.inclusive
                    }
                }
                is Event.NavigateUp -> navController.navigateUp()
                is Event.Dialog -> if (event.dialog == DialogState.Error) {
                    errorDialogOpen = event.action == DialogAction.Open
                    if (errorDialogOpen) errorMessage = event.description
                }
            }
        }
    }

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

                    FDropDownMenu(
                        modifier = Modifier.fillMaxWidth(),
                        options = categoryEntityList.map { it.name },
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


                items(taskEntityList) { task ->
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

