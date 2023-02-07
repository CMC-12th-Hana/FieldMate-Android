package com.hana.umuljeong.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
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
import com.hana.umuljeong.isValidPassword
import com.hana.umuljeong.ui.component.UAppBarWithBackBtn
import com.hana.umuljeong.ui.component.UButton
import com.hana.umuljeong.ui.component.UTextField
import com.hana.umuljeong.ui.theme.ButtonSkyBlue
import com.hana.umuljeong.ui.theme.ErrorRed
import com.hana.umuljeong.ui.theme.FontDarkGray
import com.hana.umuljeong.ui.theme.UmuljeongTheme

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
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
        var name by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var phoneNumber by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }

        val passwordCondition = mutableListOf(false, false, false, false)
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

                Row {
                    Text(
                        text = stringResource(id = R.string.name),
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

                Spacer(modifier = Modifier.height(4.dp))

                UTextField(
                    modifier = Modifier.width(335.dp),
                    msgContent = name,
                    hint = stringResource(id = R.string.name_hint),
                    onValueChange = { name = it }
                )

                Spacer(modifier = Modifier.height(26.dp))

                Row {
                    Text(
                        text = stringResource(id = R.string.email),
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

                Spacer(modifier = Modifier.height(4.dp))

                UTextField(
                    modifier = Modifier.width(335.dp),
                    msgContent = email,
                    hint = stringResource(id = R.string.email_hint),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    onValueChange = { email = it }
                )

                Spacer(modifier = Modifier.height(26.dp))

                Row {
                    Text(
                        text = stringResource(id = R.string.phone_number),
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

                Spacer(modifier = Modifier.height(4.dp))

                UTextField(
                    modifier = Modifier.width(335.dp),
                    msgContent = phoneNumber,
                    hint = stringResource(id = R.string.phone_number_hint),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone
                    ),
                    onValueChange = { phoneNumber = it }
                )

                Spacer(modifier = Modifier.height(26.dp))

                Row {
                    Text(
                        text = stringResource(id = R.string.password),
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

                Spacer(modifier = Modifier.height(4.dp))

                UTextField(
                    modifier = Modifier.width(335.dp),
                    msgContent = password,
                    hint = stringResource(id = R.string.password_hint),
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
                        color = FontDarkGray
                    )
                )

                if (password.isNotEmpty()) {
                    passwordCondition[0] = isValidPassword(password, "^.{8,20}$")
                    passwordCondition[1] = isValidPassword(password, "^(?=.*[a-z])(?=.*[A-Z]).+")
                    passwordCondition[2] = isValidPassword(password, "^(?=.*[0-9]).+")
                    passwordCondition[3] =
                        isValidPassword(password, """^(?=.*[-+_!@#\$%^&*., ?]).+""")

                    PasswordConditionMessage(passwordCondition = passwordCondition)
                }

                Spacer(modifier = Modifier.height(26.dp))

                Row {
                    Text(
                        text = stringResource(id = R.string.confirm_password),
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

                Spacer(modifier = Modifier.height(4.dp))

                UTextField(
                    modifier = Modifier.width(335.dp),
                    msgContent = confirmPassword,
                    hint = stringResource(id = R.string.confirm_password_hint),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    onValueChange = { confirmPassword = it }
                )

                if (confirmPassword.isNotEmpty()) {
                    confirmPasswordCondition = password == confirmPassword
                    ConfirmPasswordConditionMessage(confirmPasswordCondition = confirmPasswordCondition)
                }

                Spacer(modifier = Modifier.height(26.dp))
            }

            val buttonEnabled =
                name.isNotEmpty() && email.isNotEmpty() && phoneNumber.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && confirmPasswordCondition

            Column {
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
fun PasswordConditionMessage(
    modifier: Modifier = Modifier,
    passwordCondition: List<Boolean>
) {
    Spacer(modifier = Modifier.height(8.dp))

    val messages = listOf(
        stringResource(id = R.string.password_condition_first),
        stringResource(id = R.string.password_condition_second),
        stringResource(id = R.string.password_condition_third),
        stringResource(id = R.string.password_condition_fourth)
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (i: Int in messages.indices) {
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    painter = if (passwordCondition[i]) painterResource(id = R.drawable.ic_check)
                    else painterResource(id = R.drawable.ic_error),
                    tint = Color.Unspecified,
                    contentDescription = null
                )
                Text(
                    text = messages[i],
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = if (passwordCondition[i]) ButtonSkyBlue else ErrorRed
                    )
                )
            }
        }
    }

}

@Composable
fun ConfirmPasswordConditionMessage(
    modifier: Modifier = Modifier,
    confirmPasswordCondition: Boolean
) {
    Spacer(modifier = Modifier.height(8.dp))

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            painter = if (confirmPasswordCondition) painterResource(id = R.drawable.ic_check)
            else painterResource(id = R.drawable.ic_error),
            tint = Color.Unspecified,
            contentDescription = null
        )
        Text(
            text = stringResource(id = R.string.confirm_password_condition),
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = if (confirmPasswordCondition) ButtonSkyBlue else ErrorRed
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
            registerBtnOnClick = { }
        )
    }
}