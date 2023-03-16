package com.hana.fieldmate.ui.member

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hana.fieldmate.R
import com.hana.fieldmate.data.local.UserInfo
import com.hana.fieldmate.ui.DialogAction
import com.hana.fieldmate.ui.DialogState
import com.hana.fieldmate.ui.Event
import com.hana.fieldmate.ui.component.*
import com.hana.fieldmate.ui.member.viewmodel.MemberUiState
import com.hana.fieldmate.ui.theme.Font70747E
import com.hana.fieldmate.ui.theme.Typography
import com.hana.fieldmate.ui.theme.body4
import com.hana.fieldmate.util.LEADER
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun EditMemberScreen(
    modifier: Modifier = Modifier,
    uiState: MemberUiState,
    userInfo: UserInfo,
    eventsFlow: Flow<Event>,
    sendEvent: (Event) -> Unit,
    loadMember: () -> Unit,
    navController: NavController,
    updateMyProfile: (String, String, String) -> Unit,
    updateMemberProfile: (String, String, String, String) -> Unit
) {
    val memberEntity = uiState.member

    var name by rememberSaveable { mutableStateOf("") }
    var phoneNumber by rememberSaveable { mutableStateOf("") }
    var staffRank by rememberSaveable { mutableStateOf("") }
    var staffNumber by rememberSaveable { mutableStateOf("") }

    var errorDialogOpen by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf("") }

    if (errorDialogOpen) ErrorDialog(
        errorMessage = errorMessage,
        onClose = { sendEvent(Event.Dialog(DialogState.Error, DialogAction.Close)) }
    )

    LaunchedEffect(memberEntity) {
        name = memberEntity.name
        phoneNumber = memberEntity.phoneNumber
        staffRank = memberEntity.staffRank
        staffNumber = memberEntity.staffNumber
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
                title = stringResource(id = R.string.edit_profile),
                backBtnOnClick = {
                    navController.navigateUp()
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            LoadingContent(uiState.memberLoadingState) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(40.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(contentAlignment = Alignment.BottomEnd) {
                            val context = LocalContext.current

                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(memberEntity.profileImg)
                                    .build(),
                                modifier = Modifier.size(70.dp),
                                filterQuality = FilterQuality.Low,
                                contentScale = ContentScale.Crop,
                                contentDescription = null
                            )

                            Icon(
                                modifier = Modifier.clickable(
                                    onClick = { }
                                ),
                                painter = painterResource(id = R.drawable.ic_gray_edit),
                                tint = Color.Unspecified,
                                contentDescription = null
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.name),
                        style = Typography.body4
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    FTextField(
                        modifier = Modifier.fillMaxWidth(),
                        msgContent = name,
                        onValueChange = { name = it }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.phone),
                        style = Typography.body4
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    FTextField(
                        modifier = Modifier.fillMaxWidth(),
                        msgContent = phoneNumber,
                        enabled = userInfo.userRole == LEADER,
                        onValueChange = { phoneNumber = it }
                    )

                    if (userInfo.userRole != "리더") {
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_info),
                                tint = Color.Black,
                                contentDescription = null
                            )

                            Text(
                                text = stringResource(id = R.string.change_phone_info),
                                style = Typography.body4,
                                color = Font70747E
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.member_rank),
                        style = Typography.body4
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    FTextField(
                        modifier = Modifier.fillMaxWidth(),
                        msgContent = staffRank,
                        onValueChange = { staffRank = it }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.member_number),
                        style = Typography.body4
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    FTextField(
                        modifier = Modifier.fillMaxWidth(),
                        msgContent = staffNumber,
                        onValueChange = { staffNumber = it }
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
            Column {
                Spacer(modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f))

                Spacer(modifier = Modifier.height(40.dp))

                FButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp),
                    text = stringResource(id = R.string.edit_complete),
                    onClick = {
                        if (userInfo.userRole == LEADER) updateMemberProfile(
                            name,
                            phoneNumber,
                            staffNumber,
                            staffRank
                        )
                        else updateMyProfile(name, staffNumber, staffRank)
                    }
                )

                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }
}
