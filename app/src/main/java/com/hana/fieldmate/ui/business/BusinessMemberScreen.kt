package com.hana.fieldmate.ui.business

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hana.fieldmate.FieldMateScreen
import com.hana.fieldmate.R
import com.hana.fieldmate.domain.model.MemberNameEntity
import com.hana.fieldmate.domain.toMemberEntity
import com.hana.fieldmate.ui.DialogAction
import com.hana.fieldmate.ui.DialogState
import com.hana.fieldmate.ui.Event
import com.hana.fieldmate.ui.business.viewmodel.BusinessUiState
import com.hana.fieldmate.ui.component.ErrorDialog
import com.hana.fieldmate.ui.component.FAppBarWithEditBtn
import com.hana.fieldmate.ui.component.JwtExpiredDialog
import com.hana.fieldmate.ui.component.LoadingContent
import com.hana.fieldmate.ui.member.MemberItem
import com.hana.fieldmate.ui.theme.Font191919
import com.hana.fieldmate.ui.theme.Shapes
import com.hana.fieldmate.ui.theme.Typography
import com.hana.fieldmate.ui.theme.body3
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun BusinessMemberScreen(
    modifier: Modifier = Modifier,
    uiState: BusinessUiState,
    selectedMemberList: List<MemberNameEntity>,
    eventsFlow: Flow<Event>,
    sendEvent: (Event) -> Unit,
    loadBusiness: () -> Unit,
    navController: NavController
) {
    var jwtExpiredDialogOpen by remember { mutableStateOf(false) }
    var errorDialogOpen by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf("") }
    if (errorDialogOpen) ErrorDialog(
        errorMessage = errorMessage,
        onClose = { sendEvent(Event.Dialog(DialogState.Error, DialogAction.Close)) }
    ) else if (jwtExpiredDialogOpen) {
        JwtExpiredDialog(sendEvent = sendEvent)
    }

    LaunchedEffect(true) {
        loadBusiness()

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

    Scaffold(
        topBar = {
            FAppBarWithEditBtn(
                title = stringResource(id = R.string.business_members),
                backBtnOnClick = { navController.navigateUp() },
                editBtnOnClick = { navController.navigate("${FieldMateScreen.SelectBusinessMember.name}/${uiState.business.id}") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(modifier = modifier.padding(start = 20.dp, end = 20.dp)) {
                Spacer(modifier = Modifier.height(20.dp))

                LoadingContent(uiState.memberNameListLoadingState) {
                    for (member in selectedMemberList) {
                        MemberItem(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                navController.navigate("${FieldMateScreen.DetailMember.name}/${member.id}")
                            },
                            memberEntity = member.toMemberEntity()
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }

            Spacer(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            )

            Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp)) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = Shapes.large,
                    color = Font191919.copy(alpha = 0.7f),
                    elevation = 0.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 15.dp,
                                bottom = 15.dp,
                                start = 20.dp,
                                end = 20.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.select_member_hint),
                            style = Typography.body3,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }
}