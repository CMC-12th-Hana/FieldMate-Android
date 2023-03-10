package com.hana.fieldmate.ui.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hana.fieldmate.R
import com.hana.fieldmate.ui.DialogAction
import com.hana.fieldmate.ui.DialogState
import com.hana.fieldmate.ui.Event
import com.hana.fieldmate.ui.component.ErrorDialog
import com.hana.fieldmate.ui.component.FButton
import com.hana.fieldmate.ui.component.FPasswordTextField
import com.hana.fieldmate.ui.component.FTextField
import com.hana.fieldmate.ui.theme.Font70747E
import com.hana.fieldmate.ui.theme.Main356DF8
import com.hana.fieldmate.ui.theme.Typography
import com.hana.fieldmate.ui.theme.body5
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    eventsFlow: Flow<Event>,
    sendEvent: (Event) -> Unit,
    navController: NavController,
    loginBtnOnClick: (String, String) -> Unit,
    findPwBtnOnClick: () -> Unit,
    registerBtnOnClick: () -> Unit
) {
    var id by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    var errorDialogOpen by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf("") }

    if (errorDialogOpen) ErrorDialog(
        errorMessage = errorMessage,
        onClose = { errorDialogOpen = false }
    )

    LaunchedEffect(true) {
        eventsFlow.collectLatest { event ->
            when (event) {
                is Event.NavigateTo -> navController.navigate(event.destination)
                is Event.NavigatePopUpTo -> navController.navigate(event.destination) {
                    popUpTo(event.popUpDestination) {
                        inclusive = event.inclusive
                    }
                }
                is Event.NavigateUp -> navController.navigateUp()
                is Event.Dialog -> if (event.dialog == DialogState.Error) {
                    errorDialogOpen = event.action == DialogAction.Open
                    if (errorDialogOpen) errorMessage = event.description
                }
            }
        }
    }

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
                onClick = findPwBtnOnClick
            )
        )

        Spacer(Modifier.height(30.dp))

        FButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.register),
            onClick = registerBtnOnClick,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.White,
                contentColor = Main356DF8
            ),
            border = BorderStroke(width = 1.dp, color = Main356DF8)
        )
    }
}
