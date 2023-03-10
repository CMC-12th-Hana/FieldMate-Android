package com.hana.fieldmate.ui.member

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hana.fieldmate.FieldMateScreen
import com.hana.fieldmate.R
import com.hana.fieldmate.data.local.UserInfo
import com.hana.fieldmate.ui.DialogAction
import com.hana.fieldmate.ui.DialogState
import com.hana.fieldmate.ui.Event
import com.hana.fieldmate.ui.component.*
import com.hana.fieldmate.ui.member.viewmodel.MemberUiState
import com.hana.fieldmate.ui.theme.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DetailMemberScreen(
    modifier: Modifier = Modifier,
    uiState: MemberUiState,
    eventsFlow: Flow<Event>,
    sendEvent: (Event) -> Unit,
    loadMember: () -> Unit,
    deleteMember: () -> Unit,
    userInfo: UserInfo,
    navController: NavController
) {
    val member = uiState.memberEntity

    var deleteMemberDialogOpen by rememberSaveable { mutableStateOf(false) }

    if (deleteMemberDialogOpen) DeleteDialog(
        message = stringResource(id = R.string.delete_member_message),
        onClose = {
            sendEvent(Event.Dialog(DialogState.Delete, DialogAction.Close))
        },
        onConfirm = {
            sendEvent(Event.Dialog(DialogState.Delete, DialogAction.Close))
            deleteMember()
        }
    )

    var errorDialogOpen by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf("") }

    if (errorDialogOpen) ErrorDialog(
        errorMessage = errorMessage,
        onClose = { sendEvent(Event.Dialog(DialogState.Delete, DialogAction.Close)) }
    )

    LaunchedEffect(true) {
        loadMember()

        eventsFlow.collectLatest { event ->
            when (event) {
                is Event.NavigateTo -> navController.navigate(event.destination)
                is Event.NavigatePopUpTo -> navController.navigate(event.destination) {
                    popUpTo(event.popUpDestination) {
                        inclusive = event.inclusive
                    }
                }
                is Event.NavigateUp -> navController.navigateUp()
                is Event.Dialog -> if (event.dialog == DialogState.Delete) {
                    deleteMemberDialogOpen = event.action == DialogAction.Open
                } else if (event.dialog == DialogState.Error) {
                    errorDialogOpen = event.action == DialogAction.Open
                    if (errorDialogOpen) errorMessage = event.description
                }
            }
        }
    }

    Scaffold(
        topBar = {
            if (userInfo.userRole == "??????") {
                FAppBarWithDeleteBtn(
                    title = stringResource(id = R.string.detail_profile),
                    backBtnOnClick = {
                        navController.navigateUp()
                    },
                    deleteBtnOnClick = {
                        sendEvent(Event.Dialog(DialogState.Delete, DialogAction.Open))
                    }
                )
            } else {
                FAppBarWithBackBtn(
                    title = stringResource(id = R.string.detail_profile),
                    backBtnOnClick = {
                        navController.navigateUp()
                    }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier.size(70.dp),
                    painter = painterResource(id = member.profileImg),
                    tint = Color.Unspecified,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = member.name,
                    style = Typography.title2
                )

                Spacer(modifier = Modifier.height(30.dp))

                // ??????????????? ????????? ???????????? ?????? ?????? ??????
                if (userInfo.userId == member.id || userInfo.userRole == "??????") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        FButton(
                            onClick = { navController.navigate("${FieldMateScreen.EditMember.name}/${member.id}") },
                            shape = Shapes.medium,
                            text = stringResource(id = R.string.edit),
                            textStyle = Typography.body6,
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.White
                            ),
                            border = BorderStroke(width = 1.dp, color = LineDBDBDB),
                            contentPadding = PaddingValues(
                                top = 6.dp,
                                bottom = 6.dp,
                                start = 8.dp,
                                end = 8.dp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    ComplainItem(
                        modifier = Modifier.fillMaxWidth(),
                        icon = painterResource(id = R.drawable.ic_profile_company),
                        title = stringResource(id = R.string.company_name),
                        description = member.company
                    )

                    ComplainItem(
                        modifier = Modifier.fillMaxWidth(),
                        icon = painterResource(id = R.drawable.ic_profile_call),
                        title = stringResource(id = R.string.member_phone),
                        description = member.phoneNumber
                    )

                    ComplainItem(
                        modifier = Modifier.fillMaxWidth(),
                        icon = painterResource(id = R.drawable.ic_grade),
                        title = stringResource(id = R.string.member_rank),
                        description = member.staffRank
                    )

                    ComplainItem(
                        modifier = Modifier.fillMaxWidth(),
                        icon = painterResource(id = R.drawable.ic_profile_mail),
                        title = stringResource(id = R.string.member_number),
                        description = member.staffNumber
                    )
                }
            }
        }
    }
}

@Composable
fun ComplainItem(
    modifier: Modifier = Modifier,
    shape: Shape = Shapes.large,
    icon: Painter,
    title: String,
    description: String
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = BgF8F8FA,
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = icon,
                tint = Color.Unspecified,
                contentDescription = null
            )

            Text(
                text = title,
                style = Typography.body1,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = description,
                style = Typography.body2
            )
        }
    }
}
