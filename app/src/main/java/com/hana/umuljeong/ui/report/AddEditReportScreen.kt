package com.hana.umuljeong.ui.report

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
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
import com.hana.umuljeong.EditMode
import com.hana.umuljeong.R
import com.hana.umuljeong.data.datasource.fakeCategorySelectionData
import com.hana.umuljeong.ui.component.*
import com.hana.umuljeong.ui.component.imagepicker.ImageInfo
import com.hana.umuljeong.ui.component.imagepicker.ImagePickerDialog
import com.hana.umuljeong.ui.theme.BgF1F1F5
import com.hana.umuljeong.ui.theme.Font70747E
import com.hana.umuljeong.ui.theme.Pretendard
import com.hana.umuljeong.ui.theme.UmuljeongTheme

@Composable
fun AddEditReportScreen(
    modifier: Modifier = Modifier,
    mode: EditMode,
    uiState: ReportUiState,
    selectedImageList: List<ImageInfo>,
    navController: NavController,
    selectImages: (List<ImageInfo>) -> Unit,
    removeImage: (ImageInfo) -> Unit,
    confirmBtnOnClick: () -> Unit
) {
    val report = uiState.report

    var client by rememberSaveable { mutableStateOf(report.client) }
    var business by rememberSaveable { mutableStateOf(report.name) }
    var selectedCategory by rememberSaveable { mutableStateOf(report.category) }
    var content by rememberSaveable { mutableStateOf(report.content) }

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

    var searchMode by rememberSaveable { mutableStateOf(SearchMode.COMPANY) }
    var searchDialogOpen by rememberSaveable { mutableStateOf(false) }

    if (searchDialogOpen) SearchDialog(
        mode = searchMode,
        onClosed = { searchDialogOpen = false },
        onSelected = { result ->
            if (searchMode == SearchMode.COMPANY) client = result
            else business = result
            searchDialogOpen = false
        }
    )

    Scaffold(
        topBar = {
            UAppBarWithBackBtn(
                title = stringResource(id = if (mode == EditMode.Add) R.string.add_report else R.string.edit_report),
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
                Spacer(modifier = Modifier.height(30.dp))

                Column(
                    modifier = modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    UTextFieldWithArrow(
                        title = stringResource(id = R.string.client_name),
                        msgContent = client,
                        onClick = {
                            searchMode = SearchMode.COMPANY
                            searchDialogOpen = true
                        }
                    )

                    UTextFieldWithArrow(
                        title = stringResource(id = R.string.business_name),
                        msgContent = business,
                        onClick = {
                            searchMode = SearchMode.BUSINESS
                            searchDialogOpen = true
                        }
                    )

                    UDropDownMenu(
                        modifier = Modifier.fillMaxWidth(),
                        title = stringResource(id = R.string.work_category),
                        options = fakeCategorySelectionData,
                        selectedOption = selectedCategory,
                        optionOnClick = { selectedCategory = it }
                    )

                    UTextFieldWithTitle(
                        modifier = Modifier.fillMaxWidth(),
                        msgContent = report.date,
                        readOnly = true,
                        title = stringResource(id = R.string.work_date)
                    )

                    UTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 260.dp, max = Dp.Infinity),
                        textStyle = TextStyle(
                            fontFamily = Pretendard,
                            color = Font70747E,
                            fontSize = 16.sp
                        ),
                        msgContent = content,
                        hint = stringResource(id = R.string.report_content_hint),
                        singleLine = false,
                        onValueChange = { content = it }
                    )

                    UAddButton(
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

                UButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.edit_complete),
                    onClick = confirmBtnOnClick
                )

                Spacer(Modifier.height(50.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEditReportScreen() {
    UmuljeongTheme {
        AddEditReportScreen(
            navController = rememberNavController(),
            mode = EditMode.Add,
            uiState = ReportUiState(),
            selectedImageList = emptyList(),
            selectImages = { },
            removeImage = { },
            confirmBtnOnClick = { }
        )
    }
}