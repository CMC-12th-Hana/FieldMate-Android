package com.hana.fieldmate.ui.member

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import com.hana.fieldmate.R
import com.hana.fieldmate.data.local.UserInfo
import com.hana.fieldmate.ui.DialogAction
import com.hana.fieldmate.ui.DialogState
import com.hana.fieldmate.ui.Event
import com.hana.fieldmate.ui.component.*
import com.hana.fieldmate.ui.member.viewmodel.MemberUiState
import com.hana.fieldmate.ui.navigation.FieldMateScreen
import com.hana.fieldmate.ui.theme.*
import com.hana.fieldmate.util.LEADER
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
    val member = uiState.member

    var deleteMemberDialogOpen by remember { mutableStateOf(false) }
    var jwtExpiredDialogOpen by remember { mutableStateOf(false) }
    var errorDialogOpen by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf("") }
    if (errorDialogOpen) ErrorDialog(
        errorMessage = errorMessage,
        onClose = { sendEvent(Event.Dialog(DialogState.Delete, DialogAction.Close)) }
    ) else if (deleteMemberDialogOpen) DeleteDialog(
        message = stringResource(id = R.string.delete_member_message),
        onClose = {
            sendEvent(Event.Dialog(DialogState.Delete, DialogAction.Close))
        },
        onConfirm = {
            sendEvent(Event.Dialog(DialogState.Delete, DialogAction.Close))
            deleteMember()
        }
    ) else if (jwtExpiredDialogOpen) {
        BackToLoginDialog(onClose = { })
    }

    LaunchedEffect(true) {
        loadMember()

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
                is Event.Dialog -> if (event.dialog == DialogState.Delete) {
                    deleteMemberDialogOpen = event.action == DialogAction.Open
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
            if (userInfo.userRole == LEADER && userInfo.userId != member.id) {
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
            LoadingContent(loadingState = uiState.memberLoadingState) {
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

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = member.name,
                            style = Typography.title2
                        )

                        if (member.role == "리더") {
                            Spacer(modifier = Modifier.width(6.dp))

                            Surface(
                                shape = Shapes.small,
                                color = Color(0xFF102043),
                                elevation = 0.dp
                            ) {
                                Text(
                                    modifier = Modifier.padding(
                                        top = 2.dp, bottom = 2.dp, start = 5.dp, end = 5.dp
                                    ),
                                    color = Color.White,
                                    text = stringResource(id = R.string.leader),
                                    style = Typography.body6,
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    // 리더이거나 본인의 프로필일 경우 수정 가능
                    if (userInfo.userId == member.id || userInfo.userRole == LEADER) {
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
