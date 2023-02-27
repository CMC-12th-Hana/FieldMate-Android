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
import com.hana.umuljeong.R
import com.hana.umuljeong.data.datasource.fakeCategorySelectionData
import com.hana.umuljeong.getCurrentTime
import com.hana.umuljeong.ui.component.*
import com.hana.umuljeong.ui.component.imagepicker.ImageInfo
import com.hana.umuljeong.ui.component.imagepicker.ImagePickerDialog
import com.hana.umuljeong.ui.theme.BgF1F1F5
import com.hana.umuljeong.ui.theme.Font70747E
import com.hana.umuljeong.ui.theme.Pretendard
import com.hana.umuljeong.ui.theme.UmuljeongTheme

@Composable
fun AddReportScreen(
    modifier: Modifier = Modifier,
    selectedImageList: List<ImageInfo>,
    navController: NavController,
    selectImages: (List<ImageInfo>) -> Unit,
    removeImage: (ImageInfo) -> Unit,
    addBtnOnClick: () -> Unit
) {
    var client by rememberSaveable { mutableStateOf("") }
    var business by rememberSaveable { mutableStateOf("") }
    var selectedCategory by rememberSaveable { mutableStateOf("") }
    var content by rememberSaveable { mutableStateOf("") }

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
                title = stringResource(id = R.string.add_report),
                backBtnOnClick = {
                    navController.navigateUp()
                }
            )
        },
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
                    .verticalScroll(rememberScrollState())
                    .weight(1f)
            ) {
                Spacer(modifier = Modifier.height(30.dp))

                Column {
                    UTextFieldWithArrow(
                        title = stringResource(id = R.string.client_name),
                        msgContent = client,
                        onClick = {
                            searchMode = SearchMode.COMPANY
                            searchDialogOpen = true
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    UTextFieldWithArrow(
                        title = stringResource(id = R.string.business_name),
                        msgContent = business,
                        onClick = {
                            searchMode = SearchMode.BUSINESS
                            searchDialogOpen = true
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    UDropDownMenu(
                        modifier = Modifier.fillMaxWidth(),
                        title = stringResource(id = R.string.work_category),
                        options = fakeCategorySelectionData,
                        selectedOption = selectedCategory,
                        optionOnClick = { selectedCategory = it }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    UTextFieldWithTitle(
                        modifier = Modifier.fillMaxWidth(),
                        msgContent = getCurrentTime(),
                        readOnly = true,
                        title = stringResource(id = R.string.work_date)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

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

                    Spacer(modifier = Modifier.height(8.dp))

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

                    Spacer(Modifier.height(10.dp))

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
                    text = stringResource(id = R.string.complete),
                    onClick = addBtnOnClick
                )

                Spacer(Modifier.height(50.dp))
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewAddReportScreen() {
    UmuljeongTheme {
        AddReportScreen(
            navController = rememberNavController(),
            selectedImageList = emptyList(),
            selectImages = { },
            removeImage = { },
            addBtnOnClick = { }
        )
    }
}