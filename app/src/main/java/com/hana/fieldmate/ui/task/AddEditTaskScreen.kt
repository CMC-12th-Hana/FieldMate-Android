package com.hana.fieldmate.ui.task

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hana.fieldmate.EditMode
import com.hana.fieldmate.R
import com.hana.fieldmate.data.local.fakeBusinessSelectionData
import com.hana.fieldmate.data.local.fakeCategorySelectionData
import com.hana.fieldmate.data.local.fakeClientSelectionData
import com.hana.fieldmate.ui.component.*
import com.hana.fieldmate.ui.component.imagepicker.ImageInfo
import com.hana.fieldmate.ui.component.imagepicker.ImagePickerDialog
import com.hana.fieldmate.ui.theme.*

@Composable
fun AddEditTaskScreen(
    modifier: Modifier = Modifier,
    mode: EditMode,
    uiState: TaskUiState,
    selectedImageList: List<ImageInfo>,
    navController: NavController,
    selectImages: (List<ImageInfo>) -> Unit,
    removeImage: (ImageInfo) -> Unit,
    confirmBtnOnClick: () -> Unit
) {
    val task = uiState.taskEntity

    var selectedClient by rememberSaveable { mutableStateOf(task.client) }
    var selectedBusiness by rememberSaveable { mutableStateOf(task.business) }
    var title by rememberSaveable { mutableStateOf(task.title) }
    var selectedCategory by rememberSaveable { mutableStateOf(task.category) }
    var content by rememberSaveable { mutableStateOf(task.content) }

    var imagePickerOpen by rememberSaveable { mutableStateOf(false) }

    if (imagePickerOpen) ImagePickerDialog(
        maxImgCount = 10,
        selectedImageList = selectedImageList,
        onClosed = { imagePickerOpen = false },
        onSelected = { images ->
            selectImages(images)
            imagePickerOpen = false
        }
    )

    var detailImageDialogOpen by rememberSaveable { mutableStateOf(false) }
    var imageIndex by rememberSaveable { mutableStateOf(0) }

    if (detailImageDialogOpen) DetailImageDialog(
        selectedImages = selectedImageList,
        imageIndex = imageIndex,
        onClosed = { detailImageDialogOpen = false }
    )

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
                        options = fakeClientSelectionData,
                        selectedOption = selectedClient,
                        optionOnClick = { selectedClient = it }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    FDropDownMenu(
                        modifier = Modifier.fillMaxWidth(),
                        title = stringResource(id = R.string.business_name),
                        options = fakeBusinessSelectionData,
                        selectedOption = selectedBusiness,
                        optionOnClick = { selectedBusiness = it }
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
                        options = fakeCategorySelectionData,
                        selectedOption = selectedCategory,
                        optionOnClick = { selectedCategory = it }
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
                        msgContent = content,
                        hint = stringResource(id = R.string.task_content_hint),
                        singleLine = false,
                        onValueChange = { content = it }
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
                    onClick = confirmBtnOnClick
                )

                Spacer(Modifier.height(50.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEditTaskScreen() {
    FieldMateTheme {
        AddEditTaskScreen(
            navController = rememberNavController(),
            mode = EditMode.Add,
            uiState = TaskUiState(),
            selectedImageList = emptyList(),
            selectImages = { },
            removeImage = { },
            confirmBtnOnClick = { }
        )
    }
}