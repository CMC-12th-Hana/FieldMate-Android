package com.hana.fieldmate.ui.client

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hana.fieldmate.EditMode
import com.hana.fieldmate.R
import com.hana.fieldmate.ui.auth.Label
import com.hana.fieldmate.ui.component.FAppBarWithBackBtn
import com.hana.fieldmate.ui.component.FButton
import com.hana.fieldmate.ui.component.FTextField
import com.hana.fieldmate.ui.theme.*

@Composable
fun AddEditClientScreen(
    modifier: Modifier = Modifier,
    mode: EditMode,
    uiState: ClientUiState,
    navController: NavController,
    confirmBtnOnClick: (String, String, String, String, String) -> Unit
) {
    val company = uiState.clientEntity

    var name by rememberSaveable { mutableStateOf(company.name) }
    var phoneNumber by rememberSaveable { mutableStateOf(company.phone) }
    var srDepartment by rememberSaveable { mutableStateOf(company.salesRepresentativeDepartment) }
    var srName by rememberSaveable { mutableStateOf(company.salesRepresentativeName) }
    var srPhoneNumber by rememberSaveable { mutableStateOf(company.salesRepresentativePhone) }

    Scaffold(
        topBar = {
            FAppBarWithBackBtn(
                title = stringResource(id = if (mode == EditMode.Add) R.string.add_client else R.string.edit_client),
                backBtnOnClick = {
                    navController.navigateUp()
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BgF8F8FA),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(30.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp)
                    ) {
                        Label(text = stringResource(id = R.string.client_name))

                        Spacer(modifier = Modifier.height(8.dp))

                        FTextField(
                            modifier = Modifier.fillMaxWidth(),
                            msgContent = name,
                            hint = stringResource(id = R.string.client_name_hint),
                            onValueChange = { name = it })

                        Spacer(modifier = Modifier.height(20.dp))

                        Label(text = stringResource(id = R.string.client_phone))

                        Spacer(modifier = Modifier.height(8.dp))

                        FTextField(
                            modifier = Modifier.fillMaxWidth(),
                            msgContent = phoneNumber,
                            hint = stringResource(id = R.string.client_phone_hint),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            onValueChange = { phoneNumber = it })
                    }

                    Spacer(modifier = Modifier.height(30.dp))
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Spacer(modifier = Modifier.height(30.dp))

                        Text(
                            text = stringResource(id = R.string.sales_manager),
                            style = Typography.title2
                        )

                        Spacer(modifier = Modifier.height(30.dp))

                        Text(
                            text = stringResource(id = R.string.department_name),
                            style = Typography.body4
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        FTextField(
                            modifier = Modifier.fillMaxWidth(),
                            msgContent = srDepartment,
                            hint = stringResource(id = R.string.department_name_hint),
                            onValueChange = { srDepartment = it })

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = stringResource(id = R.string.manager_name_and_phone),
                            style = Typography.body4
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        FTextField(
                            modifier = Modifier.fillMaxWidth(),
                            msgContent = srName,
                            hint = stringResource(id = R.string.manager_name_hint),
                            onValueChange = { srName = it })

                        Spacer(modifier = Modifier.height(8.dp))

                        FTextField(
                            modifier = Modifier.fillMaxWidth(),
                            msgContent = srPhoneNumber,
                            hint = stringResource(id = R.string.manager_phone_hint),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            onValueChange = { srPhoneNumber = it })
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                FButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.complete),
                    onClick = {
                        confirmBtnOnClick(name, phoneNumber, srName, srPhoneNumber, srDepartment)
                    }
                )

                Spacer(Modifier.height(50.dp))
            }
        }
    }
}

@Preview
@Composable
fun PreviewEditCompanyScreen() {
    FieldMateTheme {
        AddEditClientScreen(
            mode = EditMode.Add,
            uiState = ClientUiState(),
            navController = rememberNavController(),
            confirmBtnOnClick = { _, _, _, _, _ -> }
        )
    }
}