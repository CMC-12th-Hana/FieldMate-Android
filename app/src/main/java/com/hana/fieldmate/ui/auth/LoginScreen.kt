package com.hana.fieldmate.ui.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hana.fieldmate.R
import com.hana.fieldmate.data.ErrorType
import com.hana.fieldmate.ui.DialogEvent
import com.hana.fieldmate.ui.auth.viewmodel.LoginViewModel
import com.hana.fieldmate.ui.component.*
import com.hana.fieldmate.ui.navigation.NavigateActions
import com.hana.fieldmate.ui.theme.*
import com.hana.fieldmate.util.NETWORK_CONNECTION_ERROR_MESSAGE

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState.dialog) {
        is DialogEvent.Error -> {
            when (val error = (uiState.dialog as DialogEvent.Error).errorType) {
                is ErrorType.JwtExpired -> {
                    BackToLoginDialog(onClose = { viewModel.backToLogin() })
                }
                is ErrorType.General -> {
                    if (error.errorMessage != NETWORK_CONNECTION_ERROR_MESSAGE) {
                        LoginFailedDialog(onClose = { viewModel.onDialogClosed() })
                    } else {
                        ErrorDialog(
                            errorMessage = error.errorMessage,
                            onClose = { viewModel.onDialogClosed() }
                        )
                    }
                }
            }
        }
        else -> {}
    }

    LoadingContent(loadingState = uiState.loginLoadingState) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = modifier
                    .fillMaxSize()
                    .padding(start = 20.dp, end = 20.dp)
            ) {
                var id by remember { mutableStateOf("") }
                var password by remember { mutableStateOf("") }

                Icon(
                    painter = painterResource(id = R.drawable.ic_app_logo),
                    tint = Color.Unspecified,
                    contentDescription = null
                )

                Spacer(Modifier.height(10.dp))

                FTextField(
                    modifier = Modifier.fillMaxWidth(),
                    msgContent = id,
                    hint = stringResource(id = R.string.id_input_hint),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    onValueChange = { id = it }
                )

                Spacer(Modifier.height(10.dp))

                FPasswordTextField(
                    modifier = Modifier.fillMaxWidth(),
                    msgContent = password,
                    hint = stringResource(id = R.string.pw_input_hint),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    onValueChange = { password = it }
                )

                Spacer(Modifier.height(30.dp))

                FButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.login),
                    onClick = { viewModel.login(id, password) }
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = stringResource(id = R.string.find_password),
                    style = Typography.body5,
                    color = Font70747E,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        viewModel.navigateTo(NavigateActions.LoginScreen.toFindPasswordScreen())
                    }
                )

                Spacer(Modifier.height(30.dp))

                FButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.register),
                    onClick = {
                        viewModel.navigateTo(NavigateActions.LoginScreen.toJoinScreen())
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White,
                        contentColor = Main356DF8
                    ),
                    border = BorderStroke(width = 1.dp, color = Main356DF8)
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row {
                    Text(text = "회원 가입을 하는 동시에 ", style = Typography.body5, color = Font70747E)
                    Text(
                        modifier = Modifier.clickable {
                            viewModel.navigateTo(NavigateActions.LoginScreen.toTermsOfUseScreen())
                        },
                        text = "서비스 이용약관",
                        style = Typography.body5,
                        textDecoration = TextDecoration.Underline
                    )
                    Text(text = "과", style = Typography.body5, color = Font70747E)
                }
                Row {
                    Text(
                        modifier = Modifier.clickable {
                            viewModel.navigateTo(NavigateActions.LoginScreen.toPrivacyPolicyScreen())
                        },
                        text = "개인정보 처리 방침",
                        style = Typography.body5,
                        textDecoration = TextDecoration.Underline
                    )
                    Text(text = "에 동의하고", style = Typography.body5, color = Font70747E)
                }
                Text(text = "서비스를 이용하는 것으로 간주합니다", style = Typography.body5, color = Font70747E)

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LoginFailedDialog(
    onClose: () -> Unit
) {
    FDialog(
        content = {
            Column(
                modifier = Modifier.padding(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "로그인에 실패하였습니다", style = Typography.title2, color = Main356DF8)
                Spacer(modifier = Modifier.height(15.dp))
                Text(text = "휴대폰 번호나 비밀번호를 다시 확인해 보세요", style = Typography.body3)
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
