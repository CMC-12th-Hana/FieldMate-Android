package com.hana.fieldmate.ui.task

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hana.fieldmate.EditMode
import com.hana.fieldmate.R
import com.hana.fieldmate.data.local.UserInfo
import com.hana.fieldmate.getFormattedTime
import com.hana.fieldmate.ui.DialogAction
import com.hana.fieldmate.ui.DialogState
import com.hana.fieldmate.ui.Event
import com.hana.fieldmate.ui.component.*
import com.hana.fieldmate.ui.component.imagepicker.ImageInfo
import com.hana.fieldmate.ui.component.imagepicker.ImagePickerDialog
import com.hana.fieldmate.ui.theme.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate

@Composable
fun AddEditTaskScreen(
    modifier: Modifier = Modifier,
    eventsFlow: Flow<Event>,
    sendEvent: (Event) -> Unit,
    loadTask: () -> Unit,
    loadClients: (Long) -> Unit,
    loadBusinesses: (Long) -> Unit,
    loadCategories: (Long) -> Unit,
    mode: EditMode,
    uiState: TaskUiState,
    userInfo: UserInfo,
    selectedImageList: List<ImageInfo>,
    navController: NavController,
    selectImages: (List<ImageInfo>) -> Unit,
    removeImage: (ImageInfo) -> Unit,
    confirmBtnOnClick: (Long, Long, String, String, String) -> Unit
) {
    val taskEntity = uiState.taskEntity
    val clientEntityList = uiState.clientEntityList
    val businessEntityList = uiState.businessEntityList
    val categoryEntityList = uiState.categoryEntityList

    var selectedClient by rememberSaveable { mutableStateOf(taskEntity.client) }
    var selectedClientId by rememberSaveable {
        mutableStateOf(
            clientEntityList.find { it.name == selectedClient }?.id ?: -1L
        )
    }

    var selectedBusiness by rememberSaveable { mutableStateOf(taskEntity.business) }
    var selectedBusinessId by rememberSaveable {
        mutableStateOf(
            businessEntityList.find { it.name == selectedBusiness }?.id ?: -1L
        )
    }

    var title by rememberSaveable { mutableStateOf(taskEntity.title) }

    var selectedCategory by rememberSaveable { mutableStateOf(taskEntity.category) }
    var selectedCategoryId by rememberSaveable {
        mutableStateOf(
            categoryEntityList.find { it.name == selectedCategory }?.id ?: -1L
        )
    }

    var description by rememberSaveable { mutableStateOf(taskEntity.description) }

    var imagePickerOpen by rememberSaveable { mutableStateOf(false) }

    if (imagePickerOpen) ImagePickerDialog(
        maxImgCount = 10,
        selectedImageList = selectedImageList,
        onClosed = { sendEvent(Event.Dialog(DialogState.PhotoPick, DialogAction.Close)) },
        onSelected = { images ->
            selectImages(images)
            sendEvent(Event.Dialog(DialogState.PhotoPick, DialogAction.Close))
        }
    )

    var detailImageDialogOpen by rememberSaveable { mutableStateOf(false) }
    var imageIndex by rememberSaveable { mutableStateOf(0) }

    if (detailImageDialogOpen) DetailImageDialog(
        selectedImages = selectedImageList,
        imageIndex = imageIndex,
        onClosed = { sendEvent(Event.Dialog(DialogState.Image, DialogAction.Close)) }
    )

    var errorDialogOpen by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf("") }

    if (errorDialogOpen) ErrorDialog(
        errorMessage = errorMessage,
        onClose = { sendEvent(Event.Dialog(DialogState.Error, DialogAction.Close)) }
    )

    LaunchedEffect(taskEntity) {
        title = taskEntity.title
        description = taskEntity.description
        selectedClient = taskEntity.client
        selectedBusiness = taskEntity.business
    }

    LaunchedEffect(true) {
        loadTask()
        loadCategories(userInfo.companyId)
        loadClients(userInfo.companyId)

        eventsFlow.collectLatest { event ->
            when (event) {
                is Event.NavigateTo -> navController.navigate(event.destination)
                is Event.NavigatePopUpTo -> navController.navigate(event.destination) {
                    popUpTo(event.popUpDestination) {
                        inclusive = event.inclusive
                    }
                }
                is Event.NavigateUp -> navController.navigateUp()
                is Event.Dialog -> if (event.dialog == DialogState.PhotoPick) {
                    imagePickerOpen = event.action == DialogAction.Open
                    if (imagePickerOpen) loadTask()
                } else if (event.dialog == DialogState.Error) {
                    errorDialogOpen = event.action == DialogAction.Open
                    if (errorDialogOpen) errorMessage = event.description
                }
            }
        }
    }

    // 고객사를 변경할 때마다 사업 목록을 다시 불러옴
    LaunchedEffect(selectedClientId) {
        selectedBusiness = "고객사를 먼저 선택해주세요"
        selectedBusinessId = -1L
        loadBusinesses(selectedClientId)
    }

    Scaffold(
        topBar = {
            FAppBarWithBackBtn(
                title = stringResource(id = if (mode == EditMode.Add) R.string.add_task else R.string.edit_task),
                backBtnOnClick = {
                    navController.navigateUp()
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
                            Log.d("고객사 선택", "$selectedClientId")
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
                            Log.d("사업 선택", "$selectedBusinessId")
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    FTextFieldWithTitle(
                        modifier = Modifier.fillMaxWidth(),
                        msgContent = title,
                        onValueChange = { title = it },
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
                            Log.d("카테고리 선택", "$selectedCategoryId")
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    FTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 260.dp, max = Dp.Infinity),
                        textStyle = TextStyle(
                            fontFamily = Pretendard,
                            color = Font70747E,
                            fontSize = 16.sp
                        ),
                        msgContent = description,
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
                        onClick = { imagePickerOpen = true },
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
                            detailImageDialogOpen = true
                        },
                        removeImage = removeImage,
                        selectedImages = selectedImageList
                    )
                }
            }

            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp)) {
                Spacer(Modifier.height(40.dp))

                FButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.complete),
                    onClick = {
                        confirmBtnOnClick(
                            selectedBusinessId,
                            selectedCategoryId,
                            LocalDate.now().getFormattedTime(),
                            title,
                            description
                        )
                    }
                )

                Spacer(Modifier.height(50.dp))
            }
        }
    }
}
