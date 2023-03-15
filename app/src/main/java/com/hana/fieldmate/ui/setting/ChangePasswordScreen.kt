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
import androidx.navigation.NavController
import com.hana.fieldmate.R
import com.hana.fieldmate.ui.DialogAction
import com.hana.fieldmate.ui.DialogState
import com.hana.fieldmate.ui.Event
import com.hana.fieldmate.ui.auth.ConditionMessage
import com.hana.fieldmate.ui.component.ErrorDialog
import com.hana.fieldmate.ui.component.FAppBarWithBackBtn
import com.hana.fieldmate.ui.component.FButton
import com.hana.fieldmate.ui.component.FPasswordTextField
import com.hana.fieldmate.ui.setting.viewmodel.ChangePasswordUiState
import com.hana.fieldmate.ui.theme.Font70747E
import com.hana.fieldmate.ui.theme.Pretendard
import com.hana.fieldmate.ui.theme.Typography
import com.hana.fieldmate.ui.theme.body4
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ChangePasswordScreen(
    modifier: Modifier = Modifier,
    uiState: ChangePasswordUiState,
    eventsFlow: Flow<Event>,
    sendEvent: (Event) -> Unit,
    checkPassword: (String) -> Unit,
    checkConfirmPassword: (String, String) -> Unit,
    checkConfirmEnabled: () -> Boolean,
    navController: NavController,
    confirmBtnOnClick: (String, String) -> Unit
) {
    var password by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }

    var errorDialogOpen by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    if (errorDialogOpen) ErrorDialog(
        errorMessage = errorMessage,
        onClose = { sendEvent(Event.Dialog(DialogState.Error, DialogAction.Close)) }
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

    Scaffold(
        topBar = {
            FAppBarWithBackBtn(
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
                    checkPassword(newPassword)

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
                    checkConfirmPassword(newPassword, confirmNewPassword)

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
                    enabled = checkConfirmEnabled(),
                    onClick = { confirmBtnOnClick(newPassword, confirmNewPassword) }
                )

                Spacer(Modifier.height(50.dp))
            }
        }
    }
}