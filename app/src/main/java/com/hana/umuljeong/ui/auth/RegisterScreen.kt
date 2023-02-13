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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hana.umuljeong.R
import com.hana.umuljeong.isValidString
import com.hana.umuljeong.ui.component.UAppBarWithBackBtn
import com.hana.umuljeong.ui.component.UButton
import com.hana.umuljeong.ui.component.UTextField
import com.hana.umuljeong.ui.component.UTextFieldWithTimer
import com.hana.umuljeong.ui.theme.*

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    registerDataState: RegisterDataState,
    navController: NavController,
    registerBtnOnClick: () -> Unit
) {
    Scaffold(
        topBar = {
            UAppBarWithBackBtn(
                title = R.string.register,
                backBtnOnClick = {
                    navController.navigateUp()
                }
            )
        },
    ) { innerPadding ->
        var name by rememberSaveable { mutableStateOf("") }
        var email by rememberSaveable { mutableStateOf("") }
        var phone by rememberSaveable { mutableStateOf("") }
        var certNumber by rememberSaveable { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("") }
        var confirmPassword by rememberSaveable { mutableStateOf("") }

        var nameCondition = false
        var emailCondition by rememberSaveable { mutableStateOf(false) }
        var phoneCondition by rememberSaveable { mutableStateOf(false) }
        var getCertNumber by rememberSaveable { mutableStateOf(false) }
        var remainSeconds by rememberSaveable { mutableStateOf(180) }
        var certNumberCondition by rememberSaveable { mutableStateOf(false) }
        val passwordConditionList = mutableListOf(false, false, false, false)
        var confirmPasswordCondition = false

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(1f)
            ) {
                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = stringResource(id = R.string.register_info_first),
                    style = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 24.sp
                    )
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = stringResource(id = R.string.register_info_second),
                    style = TextStyle(
                        fontSize = 14.sp
                    )
                )

                Spacer(Modifier.height(40.dp))

                Label(text = stringResource(id = R.string.name))
                Spacer(modifier = Modifier.height(4.dp))
                UTextField(
                    modifier = Modifier.width(335.dp),
                    msgContent = name,
                    hint = stringResource(id = R.string.name_hint),
                    isValid = nameCondition,
                    onValueChange = { name = it }
                )
                nameCondition = name.isNotEmpty()

                Spacer(modifier = Modifier.height(26.dp))

                Label(text = stringResource(id = R.string.email))
                Spacer(modifier = Modifier.height(4.dp))
                Row(Modifier.width(335.dp)) {
                    UTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        msgContent = email,
                        hint = stringResource(id = R.string.email_hint),
                        isValid = emailCondition,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        ),
                        onValueChange = { email = it }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    UButton(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Transparent,
                            contentColor = Main356DF8,
                            disabledBackgroundColor = Color.Transparent,
                            disabledContentColor = BgD3D3D3
                        ),
                        enabled = emailCondition,
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (emailCondition) Main356DF8 else BgD3D3D3
                        ),
                        contentPadding = PaddingValues(all = 14.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.check_dup_email),
                            fontSize = 14.sp
                        )
                    }
                }
                if (email.isNotEmpty()) {
                    emailCondition = isValidString(email, "^[a-zA-Z0-9]+@[a-zA-Z0-9]+\$")

                    Spacer(modifier = Modifier.height(8.dp))

                    if (!emailCondition) {
                        ConditionMessage(message = stringResource(id = R.string.check_email_hint))
                    }
                }

                Spacer(modifier = Modifier.height(26.dp))

                Label(text = stringResource(id = R.string.phone))
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.width(335.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    UTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        msgContent = phone,
                        hint = stringResource(id = R.string.phone_hint),
                        isValid = phoneCondition,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone
                        ),
                        onValueChange = { phone = it }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    UButton(
                        onClick = {
                            getCertNumber = true
                            remainSeconds = 180
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Transparent,
                            contentColor = Main356DF8,
                            disabledBackgroundColor = Color.Transparent,
                            disabledContentColor = BgD3D3D3
                        ),
                        enabled = phoneCondition,
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (phoneCondition) Main356DF8 else BgD3D3D3
                        ),
                        contentPadding = PaddingValues(all = 14.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.receive_cert_number),
                            fontSize = 14.sp
                        )
                    }
                }
                if (phone.isNotEmpty()) {
                    phoneCondition = isValidString(phone, "\\d{10,11}")

                    Spacer(modifier = Modifier.height(8.dp))

                    if (!phoneCondition) {
                        ConditionMessage(message = stringResource(id = R.string.check_phone_hint))
                    }
                }

                if (getCertNumber) {
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.width(335.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        UTextFieldWithTimer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            msgContent = certNumber,
                            remainSeconds = remainSeconds,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            onValueChange = { certNumber = it }
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        UButton(
                            onClick = { certNumberCondition = true },
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
                        ) {
                            Text(
                                text = stringResource(id = R.string.confirm_cert_number),
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(26.dp))

                Label(text = stringResource(id = R.string.password))
                Spacer(modifier = Modifier.height(8.dp))
                UTextField(
                    modifier = Modifier.width(335.dp),
                    msgContent = password,
                    hint = stringResource(id = R.string.password_hint),
                    isValid = passwordConditionList.count { it } == 4,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    onValueChange = { password = it }
                )
                Text(
                    text = stringResource(id = R.string.password_condition_hint),
                    style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = Font70747E
                    )
                )
                if (password.isNotEmpty()) {
                    passwordConditionList[0] = isValidString(password, "^.{8,20}$")
                    passwordConditionList[1] = isValidString(password, "^(?=.*[a-z])(?=.*[A-Z]).+")
                    passwordConditionList[2] = isValidString(password, "^(?=.*[0-9]).+")
                    passwordConditionList[3] =
                        isValidString(password, """^(?=.*[-+_!@#\$%^&*., ?]).+""")

                    val messages = listOf(
                        stringResource(id = R.string.password_condition_first),
                        stringResource(id = R.string.password_condition_second),
                        stringResource(id = R.string.password_condition_third),
                        stringResource(id = R.string.password_condition_fourth)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        for (i: Int in messages.indices) {
                            if (!passwordConditionList[i]) {
                                ConditionMessage(message = messages[i])
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(26.dp))

                Label(text = stringResource(id = R.string.confirm_password_hint))
                Spacer(modifier = Modifier.height(4.dp))
                UTextField(
                    modifier = Modifier.width(335.dp),
                    msgContent = confirmPassword,
                    hint = stringResource(id = R.string.confirm_password_hint),
                    isValid = confirmPasswordCondition,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    onValueChange = { confirmPassword = it }
                )
                if (confirmPassword.isNotEmpty()) {
                    confirmPasswordCondition = password == confirmPassword

                    Spacer(modifier = Modifier.height(8.dp))

                    if (!confirmPasswordCondition) {
                        ConditionMessage(message = stringResource(id = R.string.confirm_password_condition))
                    }
                }

                Spacer(modifier = Modifier.height(26.dp))
            }

            val buttonEnabled =
                nameCondition && emailCondition && phoneCondition && certNumberCondition && passwordConditionList.count { it } == 4 && confirmPasswordCondition

            Column {
                Spacer(Modifier.height(15.dp))

                UButton(
                    modifier = Modifier.width(335.dp),
                    enabled = buttonEnabled,
                    onClick = registerBtnOnClick
                ) {
                    Text(
                        text = stringResource(id = R.string.register)
                    )
                }

                Spacer(Modifier.height(42.dp))
            }
        }
    }
}

@Composable
fun Label(text: String) {
    Row {
        Text(
            text = text,
            style = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            )
        )
        Text(
            text = stringResource(id = R.string.star),
            style = TextStyle(
                fontWeight = FontWeight.Normal,
                color = Color.Red,
                fontSize = 14.sp
            )
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
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = ErrorFF3120
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    UmuljeongTheme {
        RegisterScreen(
            navController = rememberNavController(),
            registerDataState = RegisterDataState(),
            registerBtnOnClick = { }
        )
    }
}