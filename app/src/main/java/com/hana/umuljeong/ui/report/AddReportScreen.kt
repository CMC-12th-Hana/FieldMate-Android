package com.hana.umuljeong.ui.report

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hana.umuljeong.R
import com.hana.umuljeong.data.datasource.fakeBussinessSelectionData
import com.hana.umuljeong.data.datasource.fakeCategorySelectionData
import com.hana.umuljeong.data.datasource.fakeCustomerSelectionData
import com.hana.umuljeong.getCurrentTime
import com.hana.umuljeong.ui.component.*
import com.hana.umuljeong.ui.theme.BgF1F1F5
import com.hana.umuljeong.ui.theme.Font70747E
import com.hana.umuljeong.ui.theme.UmuljeongTheme

@Composable
fun AddReportScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    addPhotoBtnOnClick: () -> Unit,
    addBtnOnClick: () -> Unit
) {
    Scaffold(
        topBar = {
            UAppBarWithBackBtn(
                title = R.string.add_report,
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
                    .verticalScroll(rememberScrollState())
                    .weight(1f)
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    var selectedCustomer by remember { mutableStateOf("") }
                    UDropDownMenu(
                        modifier = Modifier.width(335.dp),
                        title = stringResource(id = R.string.customer_name),
                        options = fakeCustomerSelectionData,
                        selectedOption = selectedCustomer,
                        optionOnClick = { selectedCustomer = it }
                    )

                    var selectedBusiness by remember { mutableStateOf("") }
                    UDropDownMenu(
                        modifier = Modifier.width(335.dp),
                        title = stringResource(id = R.string.business_name),
                        options = fakeBussinessSelectionData,
                        selectedOption = selectedBusiness,
                        optionOnClick = { selectedBusiness = it }
                    )

                    var selectedCategory by remember { mutableStateOf("") }
                    UDropDownMenu(
                        modifier = Modifier.width(335.dp),
                        title = stringResource(id = R.string.work_category),
                        options = fakeCategorySelectionData,
                        selectedOption = selectedCategory,
                        optionOnClick = { selectedCategory = it }
                    )

                    UTextFieldWithTitle(
                        modifier = Modifier.width(335.dp),
                        msgContent = getCurrentTime(),
                        readOnly = true,
                        title = stringResource(id = R.string.work_date)
                    )

                    var content by remember { mutableStateOf("") }
                    UTextField(
                        modifier = Modifier
                            .width(335.dp)
                            .heightIn(min = 260.dp, max = Dp.Infinity),
                        textStyle = TextStyle(
                            color = Font70747E,
                            fontSize = 16.sp
                        ),
                        msgContent = content,
                        hint = stringResource(id = R.string.report_content_hint),
                        singleLine = false,
                        onValueChange = { content = it }
                    )

                    UAddButton(
                        onClick = addPhotoBtnOnClick,
                        text = stringResource(id = R.string.add_photo),
                        topBottomPadding = 10.dp,
                        icon = painterResource(id = R.drawable.ic_camera),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = BgF1F1F5,
                            contentColor = Color.Black
                        ),
                        border = BorderStroke(width = 0.dp, color = Color.Transparent),
                        modifier = Modifier.width(335.dp)
                    )

                    Spacer(Modifier.height(8.dp))
                }
            }

            Column {
                UButton(
                    modifier = Modifier.width(335.dp),
                    onClick = addBtnOnClick
                ) {
                    Text(
                        text = stringResource(id = R.string.complete)
                    )
                }

                Spacer(Modifier.height(42.dp))
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
            addPhotoBtnOnClick = { },
            addBtnOnClick = { }
        )
    }
}