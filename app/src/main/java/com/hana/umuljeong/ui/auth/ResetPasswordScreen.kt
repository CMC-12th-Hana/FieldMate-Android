package com.hana.umuljeong.ui.auth

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hana.umuljeong.R
import com.hana.umuljeong.ui.component.UAppBarWithBackBtn
import com.hana.umuljeong.ui.component.UButton
import com.hana.umuljeong.ui.component.UPasswordTextField
import com.hana.umuljeong.ui.theme.Font70747E
import com.hana.umuljeong.ui.theme.Pretendard
import com.hana.umuljeong.ui.theme.Typography
import com.hana.umuljeong.ui.theme.body4

@Composable
fun ResetPasswordScreen(
    modifier: Modifier = Modifier,
    uiState: RegisterUiState,
    checkPassword: (String) -> Unit,
    checkConfirmPassword: (String, String) -> Unit,
    navController: NavController,
    confirmBtnOnClick: () -> Unit
) {
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            UAppBarWithBackBtn(
                title = stringResource(id = R.string.change_password),
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
                    .fillMaxHeight()
                    .padding(start = 20.dp, end = 20.dp)
                    .weight(1f)
            ) {
                Spacer(modifier = Modifier.height(30.dp))

                Text(text = stringResource(id = R.string.new_password), style = Typography.body4)
                Spacer(modifier = Modifier.height(8.dp))
                UPasswordTextField(
                    modifier = Modifier.fillMaxWidth(),
                    msgContent = password,
                    hint = stringResource(id = R.string.new_password_hint),
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

                Text(
                    text = stringResource(id = R.string.confirm_new_password),
                    style = Typography.body4
                )
                Spacer(modifier = Modifier.height(4.dp))
                UPasswordTextField(
                    modifier = Modifier.fillMaxWidth(),
                    msgContent = confirmPassword,
                    hint = stringResource(id = R.string.confirm_new_password_hint),
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
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
            ) {
                Spacer(Modifier.height(40.dp))

                UButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.confirm),
                    enabled = uiState.passwordConditionList.count { it } == 4 && uiState.confirmPasswordCondition,
                    onClick = confirmBtnOnClick
                )

                Spacer(Modifier.height(50.dp))
            }
        }
    }
}