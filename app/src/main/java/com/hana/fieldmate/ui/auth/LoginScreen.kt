package com.hana.fieldmate.ui.auth

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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hana.fieldmate.App
import com.hana.fieldmate.FieldMateScreen
import com.hana.fieldmate.R
import com.hana.fieldmate.ui.DialogAction
import com.hana.fieldmate.ui.DialogState
import com.hana.fieldmate.ui.Event
import com.hana.fieldmate.ui.component.*
import com.hana.fieldmate.ui.theme.Font70747E
import com.hana.fieldmate.ui.theme.Main356DF8
import com.hana.fieldmate.ui.theme.Typography
import com.hana.fieldmate.ui.theme.body5
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    eventsFlow: Flow<Event>,
    sendEvent: (Event) -> Unit,
    navController: NavController,
    loginBtnOnClick: (String, String) -> Unit
) {
    var id by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var errorDialogOpen by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    if (errorDialogOpen) ErrorDialog(
        errorMessage = errorMessage,
        onClose = { errorDialogOpen = false }
    )

    var jwtExpiredDialogOpen by remember { mutableStateOf(false) }
    if (jwtExpiredDialogOpen) JwtExpiredDialog(sendEvent = sendEvent)

    LaunchedEffect(true) {
        val userInfo = runBlocking { App.getInstance().getDataStore().getUserInfo().first() }

        // 로그인 상태이나 회사가 정해지지 않았다면 회사 선택 화면으로
        if (userInfo.isLoggedIn && userInfo.companyId == -1L) {
            sendEvent(
                Event.NavigatePopUpTo(
                    FieldMateScreen.SelectCompany.name,
                    FieldMateScreen.Login.name,
                    inclusive = true,
                    launchOnSingleTop = true
                )
            )
            // 로그인 상태면 홈 화면으로
        } else if (userInfo.isLoggedIn) {
            sendEvent(
                Event.NavigatePopUpTo(
                    FieldMateScreen.TaskGraph.name,
                    FieldMateScreen.Login.name,
                    inclusive = true,
                    launchOnSingleTop = true
                )
            )
        }
    }

    LaunchedEffect(true) {
        eventsFlow.collectLatest { event ->
            when (event) {
                is Event.NavigateTo -> navController.navigate(event.destination)
                is Event.NavigatePopUpTo -> navController.navigate(event.destination) {
                    popUpTo(event.popUpDestination) {
                        inclusive = event.inclusive
                    }
                    launchSingleTop = event.launchOnSingleTop
                }
                is Event.NavigateUp -> navController.navigateUp()
                is Event.Dialog -> if (event.dialog == DialogState.Error) {
                    errorDialogOpen = event.action == DialogAction.Open
                    if (errorDialogOpen) errorMessage = event.description
                } else if (event.dialog == DialogState.JwtExpired) {
                    jwtExpiredDialogOpen = event.action == DialogAction.Open
                }
            }
        }
    }

    Box(contentAlignment = Alignment.BottomCenter) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp)
        ) {
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
                onClick = { loginBtnOnClick(id, password) }
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = stringResource(id = R.string.find_password),
                style = Typography.body5,
                color = Font70747E,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable(
                    onClick = {
                        navController.navigate(FieldMateScreen.FindPassword.name)
                    }
                )
            )

            Spacer(Modifier.height(30.dp))

            FButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.register),
                onClick = {
                    navController.navigate(FieldMateScreen.Join.name)
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
        )
        {
            Row {
                Text(text = "회원 가입을 하는 동시에 ", style = Typography.body5, color = Font70747E)
                Text(
                    text = "서비스 이용약관",
                    style = Typography.body5,
                    textDecoration = TextDecoration.Underline
                )
                Text(text = "과", style = Typography.body5, color = Font70747E)
            }
            Row {
                Text(
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
