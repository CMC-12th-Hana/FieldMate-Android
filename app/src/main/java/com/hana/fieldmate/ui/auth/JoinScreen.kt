package com.hana.fieldmate.ui.auth

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hana.fieldmate.R
import com.hana.fieldmate.ui.DialogAction
import com.hana.fieldmate.ui.DialogState
import com.hana.fieldmate.ui.Event
import com.hana.fieldmate.ui.component.*
import com.hana.fieldmate.ui.theme.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun JoinScreen(
    modifier: Modifier = Modifier,
    uiState: JoinUiState,
    eventsFlow: Flow<Event>,
    sendEvent: (Event) -> Unit,
    checkName: (String) -> Unit,
    checkPhone: (String) -> Unit,
    sendMessage: (String) -> Unit,
    verifyMessage: (String, String) -> Unit,
    checkTimer: () -> Unit,
    checkPassword: (String) -> Unit,
    checkConfirmPassword: (String, String) -> Unit,
    checkRegisterEnabled: () -> (Boolean),
    navController: NavController,
    joinBtnOnClick: (String, String, String, String) -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var certNumber by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    var timeOutDialogOpen by rememberSaveable { mutableStateOf(false) }

    if (uiState.remainSeconds <= 0 && uiState.timerRunning) {
        timeOutDialogOpen = true
    }

    if (timeOutDialogOpen) TimeOutDialog(
        onClose = {
            checkTimer()
            timeOutDialogOpen = false
        }
    )

    var errorDialogOpen by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf("") }

    if (errorDialogOpen) ErrorDialog(
        errorMessage = errorMessage,
        onClose = { errorDialogOpen = false }
    )

    LaunchedEffect(true) {
        eventsFlow.collectLatest { event ->
            when (event) {
                is Event.NavigateTo -> navController.navigate(event.destination)
                is Event.NavigatePopUpTo -> navController.navigate(event.destination) {
                    popUpTo(event.popUpDestination) {
                        inclusive = event.inclusive
                    }
                }
                is Event.NavigateUp -> navController.navigateUp()
                is Event.Dialog -> if (event.dialog == DialogState.TimeOut) {
                    timeOutDialogOpen = event.action == DialogAction.Open
                } else if (event.dialog == DialogState.Error) {
                    errorDialogOpen = event.action == DialogAction.Open
                    if (errorDialogOpen) errorMessage = event.description
                }
            }
        }
    }

    Scaffold(
        topBar = {
            FAppBarWithBackBtn(
                title = stringResource(id = R.string.register),
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
                    .fillMaxSize()
                    .padding(start = 20.dp, end = 20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = stringResource(id = R.string.register_info_first),
                    style = Typography.title1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(id = R.string.register_info_second),
                    style = Typography.body4
                )

                Spacer(Modifier.height(30.dp))

                Label(text = stringResource(id = R.string.name))
                Spacer(modifier = Modifier.height(4.dp))
                FTextField(
                    modifier = Modifier.fillMaxWidth(),
                    msgContent = name,
                    hint = stringResource(id = R.string.name_hint),
                    isValid = uiState.nameCondition,
                    onValueChange = {
                        name = it
                    }
                )
                checkName(name)

                Spacer(modifier = Modifier.height(20.dp))

                Label(text = stringResource(id = R.string.phone))
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        msgContent = phone,
                        hint = stringResource(id = R.string.phone_hint),
                        isValid = uiState.phoneCondition,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone
                        ),
                        onValueChange = { phone = it }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    FButton(
                        onClick = { sendMessage(phone) },
                        text = stringResource(id = R.string.receive_cert_number),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Transparent,
                            contentColor = Main356DF8,
                            disabledBackgroundColor = Color.Transparent,
                            disabledContentColor = BgD3D3D3
                        ),
                        enabled = uiState.phoneCondition,
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (uiState.phoneCondition) Main356DF8 else BgD3D3D3
                        ),
                        contentPadding = PaddingValues(all = 14.dp)
                    )
                }
                if (phone.isNotEmpty()) {
                    checkPhone(phone)

                    if (!uiState.phoneCondition) {
                        Spacer(modifier = Modifier.height(4.dp))
                        ConditionMessage(message = stringResource(id = R.string.check_phone_hint))
                    }
                }

                if (uiState.timerRunning) {
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FTextFieldWithTimer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            msgContent = certNumber,
                            remainSeconds = uiState.remainSeconds,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            onValueChange = { certNumber = it }
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        FButton(
                            onClick = { verifyMessage(phone, certNumber) },
                            text = stringResource(id = R.string.confirm_cert_number),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Transparent,
                                contentColor = Main356DF8,
                                disabledBackgroundColor = Color.Transparent,
                                disabledContentColor = BgD3D3D3
                            ),
                            border = BorderStroke(
                                width = 1.dp,
                                color = Main356DF8
                            ),
                            contentPadding = PaddingValues(all = 14.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Label(text = stringResource(id = R.string.password))
                Spacer(modifier = Modifier.height(8.dp))
                FPasswordTextField(
                    modifier = Modifier.fillMaxWidth(),
                    msgContent = password,
                    hint = stringResource(id = R.string.password_hint),
                    isValid = uiState.passwordConditionList.count { it } == 4,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    onValueChange = { password = it }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(id = R.string.password_condition_hint),
                    style = TextStyle(
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = Font70747E
                    )
                )
                if (password.isNotEmpty()) {
                    checkPassword(password)

                    val messages = listOf(
                        stringResource(id = R.string.password_condition_first),
                        stringResource(id = R.string.password_condition_second),
                        stringResource(id = R.string.password_condition_third),
                        stringResource(id = R.string.password_condition_fourth)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        for (i: Int in messages.indices) {
                            if (!uiState.passwordConditionList[i]) {
                                ConditionMessage(message = messages[i])
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Label(text = stringResource(id = R.string.confirm_password_hint))
                Spacer(modifier = Modifier.height(4.dp))
                FPasswordTextField(
                    modifier = Modifier.fillMaxWidth(),
                    msgContent = confirmPassword,
                    hint = stringResource(id = R.string.confirm_password_hint),
                    isValid = uiState.confirmPasswordCondition,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    onValueChange = { confirmPassword = it }
                )
                if (confirmPassword.isNotEmpty()) {
                    checkConfirmPassword(password, confirmPassword)

                    if (!uiState.confirmPasswordCondition) {
                        Spacer(modifier = Modifier.height(4.dp))
                        ConditionMessage(message = stringResource(id = R.string.confirm_password_condition))
                    }
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                )

                Column {
                    Spacer(Modifier.height(40.dp))

                    FButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.register),
                        enabled = checkRegisterEnabled(),
                        onClick = { joinBtnOnClick(name, phone, password, confirmPassword) }
                    )

                    Spacer(Modifier.height(50.dp))
                }
            }
        }
    }
}

@Composable
fun Label(text: String) {
    Row {
        Text(
            text = text,
            style = Typography.body4
        )
        Text(
            text = stringResource(id = R.string.star),
            style = Typography.body4,
            color = ErrorFF3120
        )
    }
}

@Composable
fun ConditionMessage(
    modifier: Modifier = Modifier,
    message: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_error),
            tint = Color.Unspecified,
            contentDescription = null
        )
        Text(
            text = message,
            style = Typography.body5,
            color = ErrorFF3120
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TimeOutDialog(
    onClose: () -> Unit
) {
    FDialog(
        onDismissRequest = { },
        content = {
            Text(
                modifier = Modifier.padding(top = 30.dp, bottom = 30.dp),
                text = stringResource(id = R.string.time_out_message),
                textAlign = TextAlign.Center,
                style = Typography.body2
            )
        },
        button = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                onClick = onClose
            ) {
                Text(
                    modifier = Modifier.padding(top = 15.dp, bottom = 15.dp),
                    text = stringResource(id = R.string.confirm),
                    style = Typography.body1,
                    textAlign = TextAlign.Center,
                    color = Main356DF8
                )
            }
        }
    )
}