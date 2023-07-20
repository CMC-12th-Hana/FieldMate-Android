package com.hana.fieldmate.ui.task

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hana.fieldmate.App
import com.hana.fieldmate.R
import com.hana.fieldmate.data.ErrorType
import com.hana.fieldmate.ui.DialogEvent
import com.hana.fieldmate.ui.component.*
import com.hana.fieldmate.ui.component.imagepicker.ImagePickerDialog
import com.hana.fieldmate.ui.navigation.EditMode
import com.hana.fieldmate.ui.navigation.NavigateActions
import com.hana.fieldmate.ui.task.viewmodel.TaskViewModel
import com.hana.fieldmate.ui.theme.*
import com.hana.fieldmate.util.DateUtil.getFormattedTime
import java.time.LocalDate

@Composable
fun AddEditTaskScreen(
    modifier: Modifier = Modifier,
    viewModel: TaskViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val userInfo = App.getInstance().getUserInfo()

    var imageIndex by remember { mutableStateOf(0) }

    when (uiState.dialog) {
        is DialogEvent.Image -> {
            DetailImageDialog(
                selectedImages = viewModel.selectedImageList,
                imageIndex = imageIndex,
                onClosed = { viewModel.onDialogClosed() }
            )
        }
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
        is DialogEvent.PhotoPick -> {
            ImagePickerDialog(
                selectedImageList = viewModel.selectedImageList,
                onClosed = { viewModel.onDialogClosed() },
                onSelected = { images ->
                    viewModel.selectImages(images)
                    viewModel.onDialogClosed()
                }
            )
        }
        else -> {}
    }

    val taskEntity = uiState.task
    val clientEntityList = uiState.clientList
    val businessEntityList = uiState.businessList
    val categoryEntityList = uiState.categoryList

    var selectedClient by remember { mutableStateOf(taskEntity.client) }
    var selectedClientId by remember {
        mutableStateOf(
            clientEntityList.find { it.name == selectedClient }?.id ?: -1L
        )
    }

    var selectedBusiness by remember { mutableStateOf(taskEntity.business) }
    var selectedBusinessId by remember {
        mutableStateOf(
            businessEntityList.find { it.name == selectedBusiness }?.id ?: -1L
        )
    }

    var title by remember { mutableStateOf(taskEntity.title) }

    var selectedCategory by remember { mutableStateOf(taskEntity.category) }
    var selectedCategoryId by remember {
        mutableStateOf(
            categoryEntityList.find { it.name == selectedCategory }?.id ?: -1L
        )
    }

    var description by remember { mutableStateOf(taskEntity.description) }

    LaunchedEffect(true) {
        viewModel.loadTask()
        viewModel.loadCategories(userInfo.companyId)
        viewModel.loadClients(userInfo.companyId)
    }

    // 업무 수정 시 업무 정보가 불러오면 clientId, businessId, categoryId를 알아냄
    LaunchedEffect(taskEntity) {
        title = taskEntity.title
        description = taskEntity.description
        selectedClient = taskEntity.client
        selectedClientId = taskEntity.clientId
        selectedBusiness = taskEntity.business
        selectedBusinessId = taskEntity.businessId
        selectedCategory = taskEntity.category
        selectedCategoryId = taskEntity.categoryId

        if (selectedBusiness == "") selectedBusiness = "고객사를 먼저 선택해주세요"
    }

    // 고객사를 변경할 때마다 사업 목록을 다시 불러옴
    LaunchedEffect(selectedClientId) {
        if (selectedClientId != -1L) {
            viewModel.loadBusinesses(selectedClientId)
            selectedBusinessId = -1L
            selectedBusiness = ""
        }
    }

    Scaffold(
        topBar = {
            FAppBarWithBackBtn(
                title = stringResource(id = if (uiState.mode == EditMode.Add) R.string.add_task else R.string.edit_task),
                backBtnOnClick = {
                    viewModel.navigateTo(NavigateActions.navigateUp())
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoadingContent(loadingState = uiState.taskLoadingState) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp)
                        .weight(1f),
                ) {
                    Spacer(modifier = Modifier.height(20.dp))

                    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
                        FDropDownMenu(
                            modifier = Modifier.fillMaxWidth(),
                            title = stringResource(id = R.string.client_name),
                            options = clientEntityList.map { it.name },
                            selectedOption = selectedClient,
                            optionOnClick = {
                                selectedClient = it
                                selectedClientId =
                                    clientEntityList.find { client -> client.name == selectedClient }?.id
                                        ?: -1L
                            }
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        FDropDownMenu(
                            modifier = Modifier.fillMaxWidth(),
                            title = stringResource(id = R.string.business_name),
                            options = businessEntityList.map { it.name },
                            selectedOption = selectedBusiness,
                            optionOnClick = {
                                selectedBusiness = it
                                selectedBusinessId =
                                    businessEntityList.find { business -> business.name == selectedBusiness }?.id
                                        ?: -1L
                            }
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        FTextFieldWithTitle(
                            modifier = Modifier.fillMaxWidth(),
                            msgContent = title,
                            onValueChange = { title = it },
                            singleLine = false,
                            title = stringResource(id = R.string.title)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        FDropDownMenu(
                            modifier = Modifier.fillMaxWidth(),
                            title = stringResource(id = R.string.work_category),
                            options = categoryEntityList.map { it.name },
                            selectedOption = selectedCategory,
                            optionOnClick = {
                                selectedCategory = it
                                selectedCategoryId =
                                    categoryEntityList.find { category -> category.name == selectedCategory }?.id
                                        ?: -1L
                            }
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        FTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 260.dp, max = Dp.Infinity),
                            msgContent = description,
                            maxChar = 300,
                            hint = stringResource(id = R.string.task_content_hint),
                            singleLine = false,
                            onValueChange = { description = it }
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

                        Spacer(modifier = Modifier.height(20.dp))

                        FAddButton(
                            onClick = {
                                viewModel.openPhotoPickerDialog()
                            },
                            text = stringResource(id = R.string.add_photo),
                            topBottomPadding = 10.dp,
                            icon = painterResource(id = R.drawable.ic_camera),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = BgF1F1F5,
                                contentColor = Color.Black
                            ),
                            border = BorderStroke(width = 0.dp, color = Color.Transparent),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(2.dp))

                        ImageSlider(
                            modifier = Modifier.fillMaxWidth(),
                            onSelect = {
                                imageIndex = it
                                viewModel.openDetailImageDialog()
                            },
                            removeImage = viewModel::unselectImage,
                            selectedImages = viewModel.selectedImageList
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
            ) {
                Spacer(Modifier.height(40.dp))

                FButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.complete),
                    onClick = {
                        if (uiState.mode == EditMode.Add) {
                            viewModel.createTask(
                                selectedBusinessId,
                                selectedCategoryId,
                                LocalDate.now().getFormattedTime(),
                                title,
                                description
                            )
                        } else {
                            viewModel.updateTask(
                                selectedBusinessId,
                                selectedCategoryId,
                                title,
                                description
                            )
                        }

                    }
                )

                Spacer(Modifier.height(50.dp))
            }
        }
    }
}
