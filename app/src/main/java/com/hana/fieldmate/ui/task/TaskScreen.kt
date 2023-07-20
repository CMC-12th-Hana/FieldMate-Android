package com.hana.fieldmate.ui.task

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hana.fieldmate.App
import com.hana.fieldmate.R
import com.hana.fieldmate.data.ErrorType
import com.hana.fieldmate.data.remote.model.TaskTypeQuery
import com.hana.fieldmate.domain.model.TaskEntity
import com.hana.fieldmate.ui.DialogEvent
import com.hana.fieldmate.ui.component.*
import com.hana.fieldmate.ui.navigation.FieldMateScreen
import com.hana.fieldmate.ui.navigation.NavigateActions
import com.hana.fieldmate.ui.setting.CategoryTag
import com.hana.fieldmate.ui.task.viewmodel.TaskListViewModel
import com.hana.fieldmate.ui.theme.*
import com.hana.fieldmate.util.DateUtil.getFormattedTime
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TaskScreen(
    modifier: Modifier = Modifier,
    viewModel: TaskListViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val userInfo = App.getInstance().getUserInfo()

    when (uiState.dialog) {
        is DialogEvent.Error -> {
            when (val error = (uiState.dialog as DialogEvent.Error).errorType) {
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

    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded },
        skipHalfExpanded = true
    )
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showTasksByMemberSwitch by remember { mutableStateOf(false) }

    LaunchedEffect(userInfo.companyId, selectedDate, showTasksByMemberSwitch) {
        if (showTasksByMemberSwitch) {
            viewModel.loadTasks(
                userInfo.companyId,
                selectedDate.getFormattedTime(),
                TaskTypeQuery.MEMBER
            )
        } else {
            viewModel.loadTasks(
                userInfo.companyId,
                selectedDate.getFormattedTime(),
                TaskTypeQuery.TASK
            )
        }
    }

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
                    onYearMonthChanged = { },
                    onDayClicked = { selectedDate = it }
                )
            }
        }
    ) {
        BackHandler(enabled = modalSheetState.isVisible) {
            coroutineScope.launch {
                modalSheetState.hide()
            }
        }

        Scaffold(
            topBar = {
                FHomeAppBar(
                    selectedDate = selectedDate,
                    onDayClicked = {
                        selectedDate = it
                        coroutineScope.launch {
                            modalSheetState.hide()
                        }
                    },
                    expandBtnOnClick = {
                        coroutineScope.launch {
                            modalSheetState.show()
                        }
                    },
                    settingBtnOnClick = {
                        viewModel.navigateTo(NavigateActions.TaskScreen.toSettingScreen())
                    }
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
                LoadingContent(loadingState = uiState.taskListLoadingState) {
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
                                    text = stringResource(id = R.string.show_member_task),
                                    style = Typography.body3,
                                    color = Font70747E
                                )

                                Spacer(modifier = Modifier.width(10.dp))

                                FSwitch(
                                    switchOn = showTasksByMemberSwitch,
                                    switchOnClick = { showTasksByMemberSwitch = it }
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))
                        }

                        item {
                            FAddButton(
                                onClick = {
                                    viewModel.navigateTo(NavigateActions.TaskScreen.toAddTaskScreen())
                                },
                                text = stringResource(id = R.string.add_task),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(20.dp))
                        }


                        if (showTasksByMemberSwitch) {
                            items(uiState.taskMemberList) { memberTask ->
                                ExpandableTaskItem(
                                    navController = navController,
                                    memberName = memberTask.memberName,
                                    taskEntityList = memberTask.taskEntityList
                                )

                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        } else {
                            items(uiState.taskList) { task ->
                                TaskItem(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = {
                                        viewModel.navigateTo(
                                            NavigateActions.TaskScreen.toDetailTaskScreen(
                                                task.id
                                            )
                                        )
                                    },
                                    taskEntity = task
                                )

                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TaskItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    showAuthor: Boolean = false,
    shape: Shape = Shapes.large,
    taskEntity: TaskEntity
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
            Column(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)) {
                Text(text = taskEntity.title, style = Typography.body2)

                if (showAuthor) {
                    Spacer(modifier = Modifier.height(6.dp))

                    Text(text = taskEntity.author, style = Typography.body4, color = Font70747E)
                }
            }


            CategoryTag(text = taskEntity.category, color = taskEntity.categoryColor)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExpandableTaskItem(
    modifier: Modifier = Modifier,
    navController: NavController,
    memberName: String,
    taskEntityList: List<TaskEntity>,
    shape: Shape = Shapes.large
) {
    var isExpanded by remember { mutableStateOf(false) }
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
                            text = "${taskEntityList.size}",
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
                    for (task in taskEntityList) {
                        Surface(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(start = 4.dp, end = 4.dp),
                            shape = Shapes.medium,
                            onClick = {
                                navController.navigate("${FieldMateScreen.DetailTask.name}/${task.id}")
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
                                    modifier = Modifier.weight(1f),
                                    text = task.title,
                                    style = Typography.body2
                                )

                                CategoryTag(text = task.category, color = task.categoryColor)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(15.dp))
                }
            }
        }
    }
}
