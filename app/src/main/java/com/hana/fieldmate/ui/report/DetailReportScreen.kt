package com.hana.fieldmate.ui.report

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hana.fieldmate.FieldMateScreen
import com.hana.fieldmate.R
import com.hana.fieldmate.ui.component.*
import com.hana.fieldmate.ui.theme.FieldMateTheme
import com.hana.fieldmate.ui.theme.Font70747E
import com.hana.fieldmate.ui.theme.Pretendard

@Composable
fun DetailReportScreen(
    modifier: Modifier = Modifier,
    uiState: ReportUiState,
    navController: NavController,
) {
    val report = uiState.reportEntity

    var detailImageDialogOpen by rememberSaveable { mutableStateOf(false) }
    var imageIndex by rememberSaveable { mutableStateOf(0) }

    if (detailImageDialogOpen) DetailImageDialog(
        selectedImages = report.images,
        imageIndex = imageIndex,
        onClosed = { detailImageDialogOpen = false }
    )

    Scaffold(
        topBar = {
            FAppBarWithEditAndDeleteBtn(
                title = stringResource(id = R.string.detail_report),
                backBtnOnClick = {
                    navController.navigateUp()
                },
                editBtnOnClick = {
                    navController.navigate("${FieldMateScreen.EditReport.name}/${uiState.reportEntity.id}")
                },
                deleteBtnOnClick = {

                }
            )
        },
    ) { innerPadding ->
        Box(modifier = modifier.padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(30.dp))

                Column(
                    modifier = modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FTextFieldWithTitle(
                        modifier = Modifier.fillMaxWidth(),
                        msgContent = report.client,
                        readOnly = true,
                        title = stringResource(id = R.string.client_name)
                    )

                    FTextFieldWithTitle(
                        modifier = Modifier.fillMaxWidth(),
                        msgContent = report.name,
                        readOnly = true,
                        title = stringResource(id = R.string.business_name)
                    )

                    FTextFieldWithTitle(
                        modifier = Modifier.fillMaxWidth(),
                        msgContent = report.category,
                        readOnly = true,
                        title = stringResource(id = R.string.work_category)
                    )

                    FTextFieldWithTitle(
                        modifier = Modifier.fillMaxWidth(),
                        msgContent = report.date,
                        readOnly = true,
                        title = stringResource(id = R.string.edit_date)
                    )

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
                        msgContent = report.content,
                        singleLine = false
                    )

                    Spacer(Modifier.height(2.dp))

                    ImageSlider(
                        modifier = Modifier.fillMaxWidth(),
                        onSelect = {
                            imageIndex = it
                            detailImageDialogOpen = true
                        },
                        selectedImages = report.images
                    )
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewDetailReportScreen() {
    FieldMateTheme {
        DetailReportScreen(
            navController = rememberNavController(),
            uiState = ReportUiState()
        )
    }
}