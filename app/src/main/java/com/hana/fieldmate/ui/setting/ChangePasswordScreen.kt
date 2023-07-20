package com.hana.fieldmate.ui.setting

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hana.fieldmate.R
import com.hana.fieldmate.data.ErrorType
import com.hana.fieldmate.ui.DialogEvent
import com.hana.fieldmate.ui.auth.ConditionMessage
import com.hana.fieldmate.ui.component.*
import com.hana.fieldmate.ui.navigation.NavigateActions
import com.hana.fieldmate.ui.setting.viewmodel.ChangePasswordViewModel
import com.hana.fieldmate.ui.theme.Font70747E
import com.hana.fieldmate.ui.theme.Pretendard
import com.hana.fieldmate.ui.theme.Typography
import com.hana.fieldmate.ui.theme.body4
import com.hana.fieldmate.util.PASSWORD_UPDATE_MESSAGE

@Composable
fun ChangePasswordScreen(
    modifier: Modifier = Modifier,
    viewModel: ChangePasswordViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }

    when (uiState.dialog) {
        is DialogEvent.Confirm -> {
            BackToLoginDialog(
                onClose = { viewModel.backToLogin() },
                message = PASSWORD_UPDATE_MESSAGE
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
        else -> {}
    }

    Scaffold(
        topBar = {
            FAppBarWithBackBtn(
                title = stringResource(id = R.string.change_password),
                backBtnOnClick = {
                    viewModel.navigateTo(NavigateActions.navigateUp())
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
                    .fillMaxHeight()
                    .padding(start = 20.dp, end = 20.dp)
                    .weight(1f)
            ) {
                Spacer(modifier = Modifier.height(30.dp))

                Text(text = stringResource(id = R.string.new_password), style = Typography.body4)
                Spacer(modifier = Modifier.height(8.dp))
                FPasswordTextField(
                    modifier = Modifier.fillMaxWidth(),
                    msgContent = newPassword,
                    hint = stringResource(id = R.string.new_password_hint),
                    isValid = uiState.passwordConditionList.count { it } == 4,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    onValueChange = { newPassword = it }
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
                if (newPassword.isNotEmpty()) {
                    viewModel.checkPassword(newPassword)

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

                Text(
                    text = stringResource(id = R.string.confirm_new_password),
                    style = Typography.body4
                )
                Spacer(modifier = Modifier.height(4.dp))
                FPasswordTextField(
                    modifier = Modifier.fillMaxWidth(),
                    msgContent = confirmNewPassword,
                    hint = stringResource(id = R.string.confirm_new_password_hint),
                    isValid = uiState.confirmPasswordCondition,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    onValueChange = { confirmNewPassword = it }
                )
                if (confirmNewPassword.isNotEmpty()) {
                    viewModel.checkConfirmPassword(newPassword, confirmNewPassword)

                    if (!uiState.confirmPasswordCondition) {
                        Spacer(modifier = Modifier.height(4.dp))
                        ConditionMessage(message = stringResource(id = R.string.confirm_password_condition))
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
            ) {
                Spacer(Modifier.height(40.dp))

                FButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.confirm),
                    enabled = viewModel.checkConfirmEnabled(),
                    onClick = { viewModel.updateMyPassword(newPassword, confirmNewPassword) }
                )

                Spacer(Modifier.height(50.dp))
            }
        }
    }
}