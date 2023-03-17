package com.hana.fieldmate.ui.member

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hana.fieldmate.R
import com.hana.fieldmate.data.local.UserInfo
import com.hana.fieldmate.ui.DialogAction
import com.hana.fieldmate.ui.DialogState
import com.hana.fieldmate.ui.Event
import com.hana.fieldmate.ui.auth.Label
import com.hana.fieldmate.ui.component.*
import com.hana.fieldmate.ui.theme.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddMemberScreen(
    modifier: Modifier = Modifier,
    eventsFlow: Flow<Event>,
    sendEvent: (Event) -> Unit,
    userInfo: UserInfo,
    navController: NavController,
    confirmBtnOnClick: (Long, String, String, String, String) -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var phoneNumber by rememberSaveable { mutableStateOf("") }
    var staffRank by rememberSaveable { mutableStateOf("") }
    var staffNumber by rememberSaveable { mutableStateOf("") }

    var addMemberAlertDialogOpen by rememberSaveable { mutableStateOf(false) }
    if (addMemberAlertDialogOpen) AddMemberAlertDialog(
        memberName = name,
        companyName = userInfo.companyName,
        onClose = {
            addMemberAlertDialogOpen = false
            sendEvent(Event.NavigateUp)
        }
    )

    var errorDialogOpen by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf("") }
    if (errorDialogOpen) ErrorDialog(
        errorMessage = errorMessage,
        onClose = { errorDialogOpen = false }
    )

    var jwtExpiredDialogOpen by remember { mutableStateOf(false) }
    if (jwtExpiredDialogOpen) JwtExpiredDialog(sendEvent = sendEvent)

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
                is Event.Dialog -> if (event.dialog == DialogState.AddEdit) {
                    addMemberAlertDialogOpen = event.action == DialogAction.Open
                } else if (event.dialog == DialogState.Error) {
                    errorDialogOpen = event.action == DialogAction.Open
                    if (errorDialogOpen) errorMessage = event.description
                } else if (event.dialog == DialogState.JwtExpired) {
                    jwtExpiredDialogOpen = event.action == DialogAction.Open
                }
            }
        }
    }

    Scaffold(
        topBar = {
            FAppBarWithBackBtn(
                title = stringResource(id = R.string.add_member),
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
                    .weight(1f)
                    .padding(start = 20.dp, end = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(30.dp))

                Label(text = stringResource(id = R.string.member_name))
                Spacer(modifier = Modifier.height(8.dp))
                FTextField(
                    modifier = Modifier.fillMaxWidth(),
                    msgContent = name,
                    hint = stringResource(id = R.string.member_name_hint),
                    onValueChange = { name = it })

                Spacer(modifier = Modifier.height(25.dp))

                Text(text = stringResource(id = R.string.member_phone), style = Typography.body4)
                Spacer(modifier = Modifier.height(8.dp))
                FTextField(
                    modifier = Modifier.fillMaxWidth(),
                    msgContent = phoneNumber,
                    hint = stringResource(id = R.string.member_phone_hint),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    onValueChange = { phoneNumber = it }
                )

                Spacer(modifier = Modifier.height(25.dp))

                Text(text = stringResource(id = R.string.member_rank), style = Typography.body4)
                Spacer(modifier = Modifier.height(8.dp))
                FTextField(
                    modifier = Modifier.fillMaxWidth(),
                    msgContent = staffRank,
                    hint = stringResource(id = R.string.member_rank_hint),
                    onValueChange = { staffRank = it }
                )

                Spacer(modifier = Modifier.height(25.dp))

                Label(text = stringResource(id = R.string.member_number))
                Spacer(modifier = Modifier.height(8.dp))
                FTextField(
                    modifier = Modifier.fillMaxWidth(),
                    msgContent = staffNumber,
                    hint = stringResource(id = R.string.member_number_hint),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    onValueChange = { staffNumber = it }
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                )

                Column {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = Shapes.large,
                        color = Font191919.copy(alpha = 0.7f),
                        elevation = 0.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 15.dp, bottom = 15.dp, start = 20.dp, end = 20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_info),
                                tint = Color.White,
                                contentDescription = null
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = stringResource(id = R.string.leader_permission_info),
                                style = Typography.body3,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    FButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.complete),
                        onClick = {
                            confirmBtnOnClick(
                                userInfo.companyId,
                                name,
                                phoneNumber,
                                staffRank,
                                staffNumber
                            )
                        }
                    )

                    Spacer(Modifier.height(50.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddMemberAlertDialog(
    modifier: Modifier = Modifier,
    memberName: String,
    companyName: String,
    onClose: () -> Unit
) {
    FDialog(
        onDismissRequest = { },
        content = {
            Text(
                modifier = Modifier.padding(all = 30.dp),
                text = "이제 ${memberName}님은 ${companyName}의 구성원입니다!\n함께 업무 관리를 시작해보세요 ",
                textAlign = TextAlign.Center,
                style = Typography.body2
            )
        },
        button = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                onClick = onClose
            ) {
                Text(
                    modifier = Modifier.padding(top = 15.dp, bottom = 15.dp),
                    text = stringResource(id = R.string.confirm),
                    style = Typography.body1,
                    textAlign = TextAlign.Center,
                    color = Main356DF8
                )
            }
        }
    )
}