package com.hana.fieldmate.ui.client

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hana.fieldmate.EditMode
import com.hana.fieldmate.R
import com.hana.fieldmate.data.local.UserInfo
import com.hana.fieldmate.ui.DialogAction
import com.hana.fieldmate.ui.DialogState
import com.hana.fieldmate.ui.Event
import com.hana.fieldmate.ui.auth.Label
import com.hana.fieldmate.ui.client.viewmodel.ClientUiState
import com.hana.fieldmate.ui.component.*
import com.hana.fieldmate.ui.theme.BgF8F8FA
import com.hana.fieldmate.ui.theme.Typography
import com.hana.fieldmate.ui.theme.body4
import com.hana.fieldmate.ui.theme.title2
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddEditClientScreen(
    modifier: Modifier = Modifier,
    eventsFlow: Flow<Event>,
    sendEvent: (Event) -> Unit,
    loadClient: () -> Unit,
    mode: EditMode,
    uiState: ClientUiState,
    userInfo: UserInfo,
    navController: NavController,
    addBtnOnClick: (Long, String, String, String, String, String) -> Unit,
    updateBtnOnClick: (String, String, String, String, String) -> Unit
) {
    val client = uiState.client

    var name by rememberSaveable { mutableStateOf("") }
    var phoneNumber by rememberSaveable { mutableStateOf("") }
    var srDepartment by rememberSaveable { mutableStateOf("") }
    var srName by rememberSaveable { mutableStateOf("") }
    var srPhoneNumber by rememberSaveable { mutableStateOf("") }

    var errorDialogOpen by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf("") }

    if (errorDialogOpen) ErrorDialog(
        errorMessage = errorMessage,
        onClose = { errorDialogOpen = false }
    )

    LaunchedEffect(client) {
        name = client.name
        phoneNumber = client.phone
        srDepartment = client.salesRepresentativeDepartment
        srName = client.salesRepresentativeName
        srPhoneNumber = client.salesRepresentativePhone
    }

    LaunchedEffect(true) {
        loadClient()

        eventsFlow.collectLatest { event ->
            when (event) {
                is Event.NavigateTo -> navController.navigate(event.destination)
                is Event.NavigatePopUpTo -> navController.navigate(event.destination) {
                    popUpTo(event.popUpDestination) {
                        inclusive = event.inclusive
                    }
                }
                is Event.NavigateUp -> navController.navigateUp()
                is Event.Dialog -> if (event.dialog == DialogState.Error) {
                    errorDialogOpen = event.action == DialogAction.Open
                    if (errorDialogOpen) errorMessage = event.description
                }
            }
        }
    }

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
            LoadingContent(loadingState = uiState.clientLoadingState) {
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
                        if (mode == EditMode.Add) {
                            addBtnOnClick(
                                userInfo.companyId,
                                name,
                                phoneNumber,
                                srName,
                                srPhoneNumber,
                                srDepartment
                            )
                        } else {
                            updateBtnOnClick(name, phoneNumber, srName, srPhoneNumber, srDepartment)
                        }
                    }
                )

                Spacer(Modifier.height(50.dp))
            }
        }
    }
}
