package com.hana.fieldmate.ui.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hana.fieldmate.FieldMateScreen
import com.hana.fieldmate.R
import com.hana.fieldmate.ui.DialogAction
import com.hana.fieldmate.ui.DialogState
import com.hana.fieldmate.ui.Event
import com.hana.fieldmate.ui.component.*
import com.hana.fieldmate.ui.theme.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DetailTaskScreen(
    modifier: Modifier = Modifier,
    uiState: TaskUiState,
    eventsFlow: Flow<Event>,
    sendEvent: (Event) -> Unit,
    loadTask: () -> Unit,
    navController: NavController,
) {
    val task = uiState.taskEntity

    var detailImageDialogOpen by rememberSaveable { mutableStateOf(false) }
    var imageIndex by rememberSaveable { mutableStateOf(0) }

    if (detailImageDialogOpen) DetailImageDialog(
        selectedImages = task.images,
        imageIndex = imageIndex,
        onClosed = { sendEvent(Event.Dialog(DialogState.Image, DialogAction.Close)) }
    )

    var deleteTaskDialogOpen by rememberSaveable { mutableStateOf(false) }

    if (deleteTaskDialogOpen) DeleteTaskDialog(
        onClose = {
            sendEvent(Event.Dialog(DialogState.Delete, DialogAction.Close))
        },
        onConfirm = {
            navController.navigateUp()
            sendEvent(Event.Dialog(DialogState.Delete, DialogAction.Close))
        }
    )

    var errorDialogOpen by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf("") }

    if (errorDialogOpen) ErrorDialog(
        errorMessage = errorMessage,
        onClose = { errorDialogOpen = false }
    )

    LaunchedEffect(true) {
        loadTask()

        eventsFlow.collectLatest { event ->
            when (event) {
                is Event.NavigateTo -> navController.navigate(event.destination)
                is Event.NavigatePopUpTo -> navController.navigate(event.destination) {
                    popUpTo(event.popUpDestination) {
                        inclusive = event.inclusive
                    }
                }
                is Event.NavigateUp -> navController.navigateUp()
                is Event.Dialog -> if (event.dialog == DialogState.Image) {
                    detailImageDialogOpen = event.action == DialogAction.Open
                } else if (event.dialog == DialogState.Delete) {
                    deleteTaskDialogOpen = event.action == DialogAction.Open
                } else if (event.dialog == DialogState.Error) {
                    errorDialogOpen = event.action == DialogAction.Open
                    if (errorDialogOpen) errorMessage = event.description
                }
            }
        }
    }

    Scaffold(
        topBar = {
            FAppBarWithEditAndDeleteBtn(
                title = stringResource(id = R.string.detail_task),
                backBtnOnClick = {
                    navController.navigateUp()
                },
                editBtnOnClick = {
                    navController.navigate("${FieldMateScreen.EditTask.name}/${uiState.taskEntity.id}")
                },
                deleteBtnOnClick = {
                    sendEvent(Event.Dialog(DialogState.Delete, DialogAction.Open))
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = modifier.padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                Column(modifier = modifier.verticalScroll(rememberScrollState())) {
                    FTextFieldWithTitle(
                        modifier = Modifier.fillMaxWidth(),
                        msgContent = task.client,
                        readOnly = true,
                        title = stringResource(id = R.string.client_name)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    FTextFieldWithTitle(
                        modifier = Modifier.fillMaxWidth(),
                        msgContent = task.business,
                        readOnly = true,
                        title = stringResource(id = R.string.business_name)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    FTextFieldWithTitle(
                        modifier = Modifier.fillMaxWidth(),
                        msgContent = task.title,
                        readOnly = true,
                        title = stringResource(id = R.string.title)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    FTextFieldWithTitle(
                        modifier = Modifier.fillMaxWidth(),
                        msgContent = task.category,
                        readOnly = true,
                        title = stringResource(id = R.string.work_category)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    FTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 260.dp, max = Dp.Infinity),
                        readOnly = true,
                        textStyle = TextStyle(
                            fontFamily = Pretendard,
                            color = Font70747E,
                            fontSize = 16.sp
                        ),
                        msgContent = task.description,
                        singleLine = false
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = stringResource(id = R.string.edit_date),
                            style = Typography.body3,
                            color = Font191919
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = task.date,
                            style = Typography.body4,
                            color = Font191919
                        )
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    ImageSlider(
                        modifier = Modifier.fillMaxWidth(),
                        onSelect = {
                            imageIndex = it
                            sendEvent(Event.Dialog(DialogState.Image, DialogAction.Open))
                        },
                        selectedImages = task.images
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DeleteTaskDialog(
    onClose: () -> Unit,
    onConfirm: () -> Unit
) {
    FDialog(
        onDismissRequest = { },
        content = {
            Text(
                modifier = Modifier.padding(top = 30.dp, bottom = 30.dp),
                text = stringResource(id = R.string.delete_task_message),
                textAlign = TextAlign.Center,
                style = Typography.body2
            )
        },
        button = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    onClick = onClose
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(id = R.string.cancel),
                            style = Typography.body1,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(
                    Modifier
                        .width(1.dp)
                        .fillMaxHeight()
                        .background(LineDBDBDB)
                )

                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    onClick = onConfirm
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(id = R.string.delete),
                            style = Typography.body1,
                            textAlign = TextAlign.Center,
                            color = ErrorFF3120
                        )
                    }
                }
            }
        }
    )
}