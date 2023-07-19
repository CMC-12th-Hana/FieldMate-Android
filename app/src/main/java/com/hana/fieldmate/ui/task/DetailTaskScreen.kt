package com.hana.fieldmate.ui.task

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hana.fieldmate.App
import com.hana.fieldmate.R
import com.hana.fieldmate.data.ErrorType
import com.hana.fieldmate.ui.DialogType
import com.hana.fieldmate.ui.component.*
import com.hana.fieldmate.ui.navigation.NavigateActions
import com.hana.fieldmate.ui.task.viewmodel.TaskViewModel
import com.hana.fieldmate.ui.theme.Font191919
import com.hana.fieldmate.ui.theme.Typography
import com.hana.fieldmate.ui.theme.body3
import com.hana.fieldmate.ui.theme.body4

@Composable
fun DetailTaskScreen(
    modifier: Modifier = Modifier,
    viewModel: TaskViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val userInfo = App.getInstance().getUserInfo()
    val taskEntity = uiState.task

    var imageIndex by rememberSaveable { mutableStateOf(0) }

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
        is DialogType.Image -> {
            DetailImageDialog(
                selectedImages = taskEntity.images,
                imageIndex = imageIndex,
                onClosed = { viewModel.onDialogClosed() }
            )
        }
        is DialogType.Delete -> {
            DeleteDialog(
                message = stringResource(id = R.string.delete_task_message),
                onClose = {
                    viewModel.onDialogClosed()
                },
                onConfirm = {
                    viewModel.deleteTask()
                    viewModel.onDialogClosed()
                }
            )
        }
        else -> {}
    }

    LaunchedEffect(true) {
        viewModel.loadTask()
    }

    Scaffold(
        topBar = {
            if (taskEntity.authorId == userInfo.userId) {
                FAppBarWithEditAndDeleteBtn(
                    title = stringResource(id = R.string.detail_task),
                    backBtnOnClick = {
                        viewModel.navigateTo(NavigateActions.navigateUp())
                    },
                    editBtnOnClick = {
                        viewModel.navigateTo(
                            NavigateActions.DetailTaskScreen.toEditTaskScreen(
                                taskEntity.id
                            )
                        )
                    },
                    deleteBtnOnClick = {
                        viewModel.openDeleteTaskDialog()
                    }
                )
            } else {
                FAppBarWithBackBtn(
                    title = stringResource(id = R.string.detail_task),
                    backBtnOnClick = {
                        viewModel.navigateTo(NavigateActions.navigateUp())
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = modifier.padding(innerPadding)) {
            LoadingContent(loadingState = uiState.taskLoadingState) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp)
                ) {
                    Spacer(modifier = Modifier.height(20.dp))

                    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
                        FTextFieldWithTitle(
                            modifier = Modifier.fillMaxWidth(),
                            msgContent = taskEntity.client,
                            readOnly = true,
                            title = stringResource(id = R.string.client_name)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        FTextFieldWithTitle(
                            modifier = Modifier.fillMaxWidth(),
                            msgContent = taskEntity.business,
                            readOnly = true,
                            title = stringResource(id = R.string.business_name)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        FTextFieldWithTitle(
                            modifier = Modifier.fillMaxWidth(),
                            msgContent = taskEntity.title,
                            readOnly = true,
                            title = stringResource(id = R.string.title)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        FTextFieldWithTitle(
                            modifier = Modifier.fillMaxWidth(),
                            msgContent = taskEntity.category,
                            readOnly = true,
                            title = stringResource(id = R.string.work_category)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        FTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 260.dp, max = Dp.Infinity),
                            readOnly = true,
                            msgContent = taskEntity.description,
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
                                text = taskEntity.date,
                                style = Typography.body4,
                                color = Font191919
                            )
                        }

                        Spacer(modifier = Modifier.height(30.dp))

                        ImageSlider(
                            modifier = Modifier.fillMaxWidth(),
                            onSelect = {
                                imageIndex = it
                                viewModel.openDetailImageDialog()
                            },
                            selectedImages = taskEntity.images
                        )

                        Spacer(modifier = Modifier.height(30.dp))
                    }
                }
            }
        }
    }
}
