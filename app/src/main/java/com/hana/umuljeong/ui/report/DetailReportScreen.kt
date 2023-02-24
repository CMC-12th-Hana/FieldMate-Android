package com.hana.umuljeong.ui.report

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hana.umuljeong.R
import com.hana.umuljeong.UmuljeongScreen
import com.hana.umuljeong.domain.Report
import com.hana.umuljeong.ui.component.UAppBarWithEditAndDeleteBtn
import com.hana.umuljeong.ui.component.UTextField
import com.hana.umuljeong.ui.component.UTextFieldWithTitle
import com.hana.umuljeong.ui.theme.Font70747E
import com.hana.umuljeong.ui.theme.Pretendard
import com.hana.umuljeong.ui.theme.UmuljeongTheme

@Composable
fun DetailReportScreen(
    modifier: Modifier = Modifier,
    uiState: ReportUiState,
    navController: NavController,
) {
    Scaffold(
        topBar = {
            UAppBarWithEditAndDeleteBtn(
                title = stringResource(id = R.string.detail_report),
                backBtnOnClick = {
                    navController.navigateUp()
                },
                editBtnOnClick = {
                    navController.navigate("${UmuljeongScreen.EditReport.name}/${uiState.report.id}")
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

                DetailReportContent(report = uiState.report)
            }
        }
    }
}

@Composable
fun DetailReportContent(
    modifier: Modifier = Modifier,
    report: Report
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        UTextFieldWithTitle(
            modifier = Modifier.fillMaxWidth(),
            msgContent = report.client,
            readOnly = true,
            title = stringResource(id = R.string.client_name)
        )

        UTextFieldWithTitle(
            modifier = Modifier.fillMaxWidth(),
            msgContent = report.name,
            readOnly = true,
            title = stringResource(id = R.string.business_name)
        )

        UTextFieldWithTitle(
            modifier = Modifier.fillMaxWidth(),
            msgContent = report.category,
            readOnly = true,
            title = stringResource(id = R.string.work_category)
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
            readOnly = true,
            textStyle = TextStyle(
                fontFamily = Pretendard,
                color = Font70747E,
                fontSize = 16.sp
            ),
            msgContent = report.content,
            singleLine = false
        )

        Spacer(Modifier.height(30.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDetailReportScreen() {
    UmuljeongTheme {
        DetailReportScreen(
            navController = rememberNavController(),
            uiState = ReportUiState()
        )
    }
}