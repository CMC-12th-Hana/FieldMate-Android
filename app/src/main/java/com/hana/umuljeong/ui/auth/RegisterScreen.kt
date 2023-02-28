package com.hana.umuljeong.ui.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hana.umuljeong.R
import com.hana.umuljeong.ui.component.*
import com.hana.umuljeong.ui.theme.*

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    uiState: RegisterUiState,
    checkName: (String) -> Unit,
    checkPhone: (String) -> Unit,
    checkCertNumber: () -> Unit,
    setTimer: (Int) -> Unit,
    checkPassword: (String) -> Unit,
    checkConfirmPassword: (String, String) -> Unit,
    checkRegisterEnabled: () -> (Boolean),
    navController: NavController,
    registerBtnOnClick: () -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var certNumber by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    var getCertNumber by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            UAppBarWithBackBtn(
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
                    .padding(start = 20.dp, end = 20.dp)
                    .verticalScroll(rememberScrollState())
                    .weight(1f)
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
                UTextField(
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
                    UTextField(
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
                    UButton(
                        onClick = {
                            getCertNumber = true
                            setTimer(180)
                        },
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

                if (getCertNumber) {
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        UTextFieldWithTimer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            msgContent = certNumber,
                            remainSeconds = uiState.remainSeconds,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            onValueChange = { certNumber = it }
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        UButton(
                            onClick = { checkCertNumber() },
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
                UPasswordTextField(
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
                UPasswordTextField(
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

                Spacer(modifier = Modifier.height(20.dp))

                Column {
                    Spacer(Modifier.height(40.dp))

                    UButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.register),
                        enabled = checkRegisterEnabled(),
                        onClick = registerBtnOnClick
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

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    UmuljeongTheme {
        RegisterScreen(
            uiState = RegisterUiState(),
            navController = rememberNavController(),
            checkName = { _ -> },
            checkPhone = { _ -> },
            checkCertNumber = { },
            setTimer = { _ -> },
            checkPassword = { _ -> },
            checkConfirmPassword = { _, _ -> },
            checkRegisterEnabled = { false },
            registerBtnOnClick = { }
        )
    }
}