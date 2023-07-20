package com.hana.fieldmate.ui.client

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hana.fieldmate.App
import com.hana.fieldmate.R
import com.hana.fieldmate.data.ErrorType
import com.hana.fieldmate.ui.DialogEvent
import com.hana.fieldmate.ui.auth.Label
import com.hana.fieldmate.ui.client.viewmodel.ClientViewModel
import com.hana.fieldmate.ui.component.*
import com.hana.fieldmate.ui.navigation.EditMode
import com.hana.fieldmate.ui.navigation.NavigateActions
import com.hana.fieldmate.ui.theme.BgF8F8FA
import com.hana.fieldmate.ui.theme.Typography
import com.hana.fieldmate.ui.theme.body4
import com.hana.fieldmate.ui.theme.title2

@Composable
fun AddEditClientScreen(
    modifier: Modifier = Modifier,
    viewModel: ClientViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val userInfo = App.getInstance().getUserInfo()
    val client = uiState.client

    when (uiState.dialog) {
        is DialogEvent.Error -> {
            when (val error = (uiState.dialog as DialogEvent.Error).errorType) {
                is ErrorType.JwtExpired -> {
                    BackToLoginDialog(onClose = { viewModel.backToLogin() })
                }
                is ErrorType.General -> {
                    ErrorDialog(
                        errorMessage = error.errorMessage,
                        onClose = { viewModel.onDialogClosed() }
                    )
                }
            }
        }
        else -> {}
    }

    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var srDepartment by remember { mutableStateOf("") }
    var srName by remember { mutableStateOf("") }
    var srPhoneNumber by remember { mutableStateOf("") }

    LaunchedEffect(client) {
        name = client.name
        phoneNumber = client.phone
        srDepartment = client.salesRepresentativeDepartment
        srName = client.salesRepresentativeName
        srPhoneNumber = client.salesRepresentativePhone
    }

    LaunchedEffect(true) {
        viewModel.loadClient()
    }

    Scaffold(
        topBar = {
            FAppBarWithBackBtn(
                title = stringResource(id = if (uiState.mode == EditMode.Add) R.string.add_client else R.string.edit_client),
                backBtnOnClick = {
                    viewModel.navigateTo(NavigateActions.navigateUp())
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            LoadingContent(loadingState = uiState.clientLoadingState) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
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
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number
                                ),
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
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number
                                ),
                                onValueChange = { srPhoneNumber = it })
                        }
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
                        if (phoneNumber.matches("""^01([016789])-?([0-9]{3,4})-?([0-9]{4})$""".toRegex())) {
                            if (uiState.mode == EditMode.Add) {
                                viewModel.createClient(
                                    userInfo.companyId,
                                    name,
                                    phoneNumber,
                                    srName,
                                    srPhoneNumber,
                                    srDepartment
                                )
                            } else {
                                viewModel.updateClient(
                                    name,
                                    phoneNumber,
                                    srName,
                                    srPhoneNumber,
                                    srDepartment
                                )
                            }
                        } else {
                            viewModel.openPhoneNumberErrorDialog()
                        }
                    }
                )

                Spacer(Modifier.height(50.dp))
            }
        }
    }
}
