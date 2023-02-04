package com.hana.umuljeong.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hana.umuljeong.R
import com.hana.umuljeong.ui.component.UButton
import com.hana.umuljeong.ui.component.UTextField
import com.hana.umuljeong.ui.theme.FontDarkGray
import com.hana.umuljeong.ui.theme.UmuljeongTheme

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    registerBtnOnClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(1f)
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = stringResource(id = R.string.register_info_one),
                style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp
                )
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = stringResource(id = R.string.register_info_two),
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

            var name by remember { mutableStateOf("") }
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

            var email by remember { mutableStateOf("") }
            UTextField(
                modifier = Modifier.width(335.dp),
                msgContent = email,
                hint = stringResource(id = R.string.email_hint),
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

            var phoneNumber by remember { mutableStateOf("") }
            UTextField(
                modifier = Modifier.width(335.dp),
                msgContent = phoneNumber,
                hint = stringResource(id = R.string.phone_number_hint),
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

            var password by remember { mutableStateOf("") }
            UTextField(
                modifier = Modifier.width(335.dp),
                msgContent = password,
                hint = stringResource(id = R.string.password_hint),
                onValueChange = { password = it }
            )
            Text(
                text = stringResource(id = R.string.password_condition),
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = FontDarkGray
                )
            )

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

            var confirmPassword by remember { mutableStateOf("") }
            UTextField(
                modifier = Modifier.width(335.dp),
                msgContent = confirmPassword,
                hint = stringResource(id = R.string.confirm_password_hint),
                onValueChange = { confirmPassword = it }
            )

            Spacer(modifier = Modifier.height(26.dp))
        }

        Column {
            UButton(
                modifier = Modifier.width(335.dp),
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

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    UmuljeongTheme {
        RegisterScreen(registerBtnOnClick = { })
    }
}