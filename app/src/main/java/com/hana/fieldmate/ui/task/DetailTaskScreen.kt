package com.hana.fieldmate.ui.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hana.fieldmate.FieldMateScreen
import com.hana.fieldmate.R
import com.hana.fieldmate.ui.component.*
import com.hana.fieldmate.ui.theme.*

@Composable
fun DetailTaskScreen(
    modifier: Modifier = Modifier,
    uiState: TaskUiState,
    navController: NavController,
) {
    val task = uiState.taskEntity

    var detailImageDialogOpen by rememberSaveable { mutableStateOf(false) }
    var imageIndex by rememberSaveable { mutableStateOf(0) }

    if (detailImageDialogOpen) DetailImageDialog(
        selectedImages = task.images,
        imageIndex = imageIndex,
        onClosed = { detailImageDialogOpen = false }
    )

    var deleteTaskDialogOpen by rememberSaveable { mutableStateOf(false) }

    if (deleteTaskDialogOpen) DeleteTaskDialog(
        onClose = {
            deleteTaskDialogOpen = false
        },
        onConfirm = {
            navController.navigateUp()
            deleteTaskDialogOpen = false
        }
    )

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
                    deleteTaskDialogOpen = true
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
                            style = com.hana.fieldmate.ui.theme.Typography.body3,
                            color = Font191919
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = task.date,
                            style = com.hana.fieldmate.ui.theme.Typography.body4,
                            color = Font191919
                        )
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    ImageSlider(
                        modifier = Modifier.fillMaxWidth(),
                        onSelect = {
                            imageIndex = it
                            detailImageDialogOpen = true
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

@Preview(showBackground = true)
@Composable
fun PreviewDetailTaskScreen() {
    FieldMateTheme {
        DetailTaskScreen(
            navController = rememberNavController(),
            uiState = TaskUiState()
        )
    }
}