package com.hana.umuljeong.ui.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hana.umuljeong.R
import com.hana.umuljeong.ui.component.UButton
import com.hana.umuljeong.ui.component.UTextField
import com.hana.umuljeong.ui.theme.Main356DF8
import com.hana.umuljeong.ui.theme.UmuljeongTheme

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    loginBtnOnClick: () -> Unit,
    findPwBtnOnClick: () -> Unit,
    registerBtnOnClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_app_logo),
            tint = Color.Unspecified,
            contentDescription = null
        )

        Spacer(Modifier.height(50.dp))

        var id by remember { mutableStateOf("") }
        UTextField(
            modifier = Modifier.width(335.dp),
            msgContent = id,
            hint = stringResource(id = R.string.id_input_hint),
            onValueChange = { id = it }
        )

        Spacer(Modifier.height(10.dp))

        var password by remember { mutableStateOf("") }
        UTextField(
            modifier = Modifier.width(335.dp),
            msgContent = password,
            hint = stringResource(id = R.string.pw_input_hint),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = { password = it }
        )

        Spacer(Modifier.height(50.dp))

        UButton(
            modifier = Modifier.width(335.dp),
            onClick = loginBtnOnClick
        ) {
            Text(
                text = stringResource(id = R.string.login)
            )
        }

        Spacer(Modifier.height(10.dp))

        Text(
            text = stringResource(id = R.string.find_password),
            fontSize = 14.sp,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable(
                onClick = findPwBtnOnClick
            )
        )

        Spacer(Modifier.height(35.dp))

        UButton(
            modifier = Modifier.width(335.dp),
            onClick = registerBtnOnClick,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.White,
                contentColor = Main356DF8
            ),
            border = BorderStroke(width = 1.dp, color = Main356DF8)
        ) {
            Text(
                text = stringResource(id = R.string.register)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    UmuljeongTheme {
        LoginScreen(loginBtnOnClick = { }, findPwBtnOnClick = { }, registerBtnOnClick = { })
    }
}