package com.hana.umuljeong.ui.report

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import com.hana.umuljeong.data.model.Report
import com.hana.umuljeong.ui.component.UAppBarWithEditBtn
import com.hana.umuljeong.ui.component.UTextField
import com.hana.umuljeong.ui.component.UTextFieldWithTitle
import com.hana.umuljeong.ui.theme.Font70747E
import com.hana.umuljeong.ui.theme.UmuljeongTheme

@Composable
fun DetailReportScreen(
    modifier: Modifier = Modifier,
    uiState: ReportUiState,
    navController: NavController,
) {
    Scaffold(
        topBar = {
            UAppBarWithEditBtn(
                title = stringResource(id = R.string.detail_report),
                editId = uiState.report.id,
                backBtnOnClick = {
                    navController.navigateUp()
                },
                editBtnOnClick = {
                    navController.navigate("${UmuljeongScreen.EditReport.name}/${uiState.report.id}")
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
                    .verticalScroll(rememberScrollState())
                    .weight(1f)
            ) {
                Spacer(modifier = Modifier.height(30.dp))

                DetailReportContent(report = uiState.report)
            }
        }
    }
}

@Composable
fun DetailReportContent(report: Report) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        UTextFieldWithTitle(
            modifier = Modifier.width(335.dp),
            msgContent = report.customer,
            readOnly = true,
            title = stringResource(id = R.string.customer_name)
        )

        UTextFieldWithTitle(
            modifier = Modifier.width(335.dp),
            msgContent = report.name,
            readOnly = true,
            title = stringResource(id = R.string.business_name)
        )

        UTextFieldWithTitle(
            modifier = Modifier.width(335.dp),
            msgContent = report.category,
            readOnly = true,
            title = stringResource(id = R.string.work_category)
        )

        UTextFieldWithTitle(
            modifier = Modifier.width(335.dp),
            msgContent = report.date,
            readOnly = true,
            title = stringResource(id = R.string.work_date)
        )

        UTextField(
            modifier = Modifier
                .width(335.dp)
                .heightIn(min = 260.dp, max = Dp.Infinity),
            readOnly = true,
            textStyle = TextStyle(
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