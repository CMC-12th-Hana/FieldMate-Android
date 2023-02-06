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
import com.hana.umuljeong.data.datasource.fakeReportData
import com.hana.umuljeong.ui.component.UAppBarWithEditBtn
import com.hana.umuljeong.ui.component.UTextField
import com.hana.umuljeong.ui.component.UTextFieldWithTitle
import com.hana.umuljeong.ui.theme.FontDarkGray
import com.hana.umuljeong.ui.theme.UmuljeongTheme

@Composable
fun DetailReportScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    reportId: Long
) {
    Scaffold(
        topBar = {
            UAppBarWithEditBtn(
                title = R.string.detail_report,
                editId = reportId,
                backBtnOnClick = {
                    navController.navigateUp()
                },
                editBtnOnClick = {
                    navController.navigate("${UmuljeongScreen.EditReport.name}/${reportId}")
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
                Spacer(modifier = Modifier.height(40.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    UTextFieldWithTitle(
                        modifier = Modifier.width(335.dp),
                        msgContent = fakeReportData[reportId.toInt()].customer,
                        readOnly = true,
                        title = stringResource(id = R.string.customer_name)
                    )

                    UTextFieldWithTitle(
                        modifier = Modifier.width(335.dp),
                        msgContent = fakeReportData[reportId.toInt()].name,
                        readOnly = true,
                        title = stringResource(id = R.string.business_name)
                    )

                    UTextFieldWithTitle(
                        modifier = Modifier.width(335.dp),
                        msgContent = fakeReportData[reportId.toInt()].category,
                        readOnly = true,
                        title = stringResource(id = R.string.work_category)
                    )

                    UTextFieldWithTitle(
                        modifier = Modifier.width(335.dp),
                        msgContent = fakeReportData[reportId.toInt()].date,
                        readOnly = true,
                        title = stringResource(id = R.string.work_date)
                    )

                    UTextField(
                        modifier = Modifier
                            .width(335.dp)
                            .heightIn(min = 260.dp, max = Dp.Infinity),
                        readOnly = true,
                        textStyle = TextStyle(
                            color = FontDarkGray,
                            fontSize = 16.sp
                        ),
                        msgContent = fakeReportData[reportId.toInt()].content,
                        singleLine = false
                    )

                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDetailReportScreen() {
    UmuljeongTheme {
        DetailReportScreen(
            navController = rememberNavController(),
            reportId = 0L
        )
    }
}