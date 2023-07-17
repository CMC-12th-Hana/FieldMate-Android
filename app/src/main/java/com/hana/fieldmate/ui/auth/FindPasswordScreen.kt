package com.hana.fieldmate.ui.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hana.fieldmate.R
import com.hana.fieldmate.data.ErrorType
import com.hana.fieldmate.data.remote.model.request.MessageType
import com.hana.fieldmate.ui.DialogType
import com.hana.fieldmate.ui.auth.viewmodel.JoinViewModel
import com.hana.fieldmate.ui.component.*
import com.hana.fieldmate.ui.navigation.NavigateActions
import com.hana.fieldmate.ui.theme.*

@Composable
fun FindPasswordScreen(
    modifier: Modifier = Modifier,
    viewModel: JoinViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState.dialog) {
        is DialogType.Confirm -> {
            ResetPasswordDialog(
                onClose = {
                    viewModel.onDialogClosed()
                    viewModel.navigateTo(NavigateActions.navigateUp())
                }
            )
        }
        is DialogType.Error -> {
            when (val error = (uiState.dialog as DialogType.Error).errorType) {
                is ErrorType.JwtExpired -> {
                    BackToLoginDialog(onClose = {
                        viewModel.backToLogin()
                    })
                }
                is ErrorType.General -> {
                    ErrorDialog(
                        errorMessage = error.errorMessage,
                        onClose = { viewModel.onDialogClosed() }
                    )
                }
            }
        }
        is DialogType.TimeOut -> {
            TimeOutDialog(
                onClose = {
                    viewModel.turnOffTimer()
                    viewModel.onDialogClosed()
                }
            )
        }
        else -> {}
    }

    var phone by remember { mutableStateOf("") }
    var certNumber by remember { mutableStateOf("") }


    if (uiState.remainSeconds <= 0 && uiState.timerRunning) {
        viewModel.timeOut()
    }

    Scaffold(
        topBar = {
            FAppBarWithBackBtn(
                title = stringResource(id = R.string.find_password),
                backBtnOnClick = { viewModel.navigateTo(NavigateActions.navigateUp()) }
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
                    .fillMaxHeight()
                    .padding(start = 20.dp, end = 20.dp)
                    .weight(1f)
            ) {
                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = stringResource(id = R.string.find_password_info_first),
                    style = Typography.title1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(id = R.string.find_password_info_second),
                    style = Typography.body4
                )

                Spacer(modifier = Modifier.height(50.dp))

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
                        onClick = { viewModel.sendMessage(phone, MessageType.PASSWORD) },
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
                                    MessageType.PASSWORD
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
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ResetPasswordDialog(
    onClose: () -> Unit
) {
    FDialog(
        content = {
            Column(
                modifier = Modifier.padding(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row {
                    Text(text = "비밀번호 재설정을 위한 ", style = Typography.body3)
                    Text(text = "임시 비밀번호", style = Typography.body3, color = Main356DF8)
                    Text(text = "가", style = Typography.body3)
                }
                Text(text = "문자로 전송되었습니다.", style = Typography.body3)
                Text(text = "변경된 비밀번호로 다시 로그인해주세요!", style = Typography.body3)
            }
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