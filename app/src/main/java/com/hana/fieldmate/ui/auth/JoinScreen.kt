package com.hana.fieldmate.ui.auth

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hana.fieldmate.R
import com.hana.fieldmate.data.ErrorType
import com.hana.fieldmate.data.remote.model.request.MessageType
import com.hana.fieldmate.ui.DialogEvent
import com.hana.fieldmate.ui.auth.viewmodel.JoinViewModel
import com.hana.fieldmate.ui.component.*
import com.hana.fieldmate.ui.navigation.NavigateActions
import com.hana.fieldmate.ui.theme.*
import com.hana.fieldmate.util.AUTHENTICATED_MESSAGE

@Composable
fun JoinScreen(
    modifier: Modifier = Modifier,
    viewModel: JoinViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState.dialog) {
        is DialogEvent.Confirm -> {
            ErrorDialog(
                errorMessage = AUTHENTICATED_MESSAGE,
                onClose = { viewModel.onDialogClosed() }
            )
        }
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
        is DialogEvent.TimeOut -> {
            TimeOutDialog(
                onClose = {
                    viewModel.turnOffTimer()
                    viewModel.onDialogClosed()
                }
            )
        }
        else -> {}
    }

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var certNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    if (uiState.remainSeconds <= 0 && uiState.timerRunning) {
        viewModel.timeOut()
    }

    Scaffold(
        topBar = {
            FAppBarWithBackBtn(
                title = stringResource(id = R.string.register),
                backBtnOnClick = { viewModel.navigateTo(NavigateActions.navigateUp()) }
            )
        }
    ) { innerPadding ->
        LoadingContent(loadingState = uiState.joinLoadingState) {
            Column(
                modifier = modifier
                    .fillMaxSize()
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
                        style = Typography.body4,
                        color = Font70747E
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
                    viewModel.checkName(name)

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
                                keyboardType = KeyboardType.Number
                            ),
                            onValueChange = { phone = it }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        FButton(
                            onClick = { viewModel.sendMessage(phone, MessageType.JOIN) },
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
                        viewModel.checkPhone(phone)

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
                                onClick = {
                                    viewModel.verifyMessage(
                                        phone,
                                        certNumber,
                                        MessageType.JOIN
                                    )
                                },
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
                        viewModel.checkPassword(password)

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
                        viewModel.checkConfirmPassword(password, confirmPassword)

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
                            enabled = viewModel.checkRegisterEnabled(),
                            onClick = { viewModel.join(name, phone, password, confirmPassword) }
                        )

                        Spacer(Modifier.height(50.dp))
                    }
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
                modifier = Modifier.padding(all = 30.dp),
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