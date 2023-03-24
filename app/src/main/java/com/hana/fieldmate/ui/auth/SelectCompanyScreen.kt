package com.hana.fieldmate.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hana.fieldmate.App
import com.hana.fieldmate.FieldMateScreen
import com.hana.fieldmate.R
import com.hana.fieldmate.data.local.UserInfo
import com.hana.fieldmate.ui.DialogAction
import com.hana.fieldmate.ui.DialogState
import com.hana.fieldmate.ui.Event
import com.hana.fieldmate.ui.component.*
import com.hana.fieldmate.ui.theme.Font191919
import com.hana.fieldmate.ui.theme.Main356DF8
import com.hana.fieldmate.ui.theme.Typography
import com.hana.fieldmate.ui.theme.body3
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking

@Composable
fun SelectCompanyScreen(
    modifier: Modifier = Modifier,
    fetchUserInfo: () -> Unit,
    eventsFlow: Flow<Event>,
    sendEvent: (Event) -> Unit,
    navController: NavController,
    joinCompany: () -> Unit
) {
    var jwtExpiredDialogOpen by remember { mutableStateOf(false) }
    var selectCompanyDialogOpen by remember { mutableStateOf(false) }
    var errorDialogOpen by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf("") }
    if (errorDialogOpen) ErrorDialog(
        errorMessage = errorMessage,
        onClose = { sendEvent(Event.Dialog(DialogState.Error, DialogAction.Close)) }
    ) else if (jwtExpiredDialogOpen) {
        BackToLoginDialog(sendEvent = sendEvent)
    } else if (selectCompanyDialogOpen) {
        SelectCompanyDialog(
            userInfo = App.getInstance().getUserInfo(),
            onSelect = joinCompany,
            onClose = { sendEvent(Event.Dialog(DialogState.Select, DialogAction.Close)) }
        )
    }

    LaunchedEffect(true) {
        runBlocking { fetchUserInfo() }

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
                } else if (event.dialog == DialogState.Select) {
                    selectCompanyDialogOpen = event.action == DialogAction.Open
                    if (selectCompanyDialogOpen) errorMessage = event.description
                } else if (event.dialog == DialogState.JwtExpired) {
                    jwtExpiredDialogOpen = event.action == DialogAction.Open
                }
            }
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FImageButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.45f, true),
                imageModifier = modifier.size(width = 120.dp, height = 120.dp),
                onClick = { sendEvent(Event.NavigateTo(FieldMateScreen.AddCompany.name)) },
                title = stringResource(id = R.string.add_company),
                description = stringResource(id = R.string.add_company_info_one),
                image = R.drawable.img_add_company
            )

            Spacer(modifier = Modifier.height(10.dp))

            FImageButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.45f, true),
                imageModifier = modifier.size(width = 120.dp, height = 120.dp),
                onClick = { sendEvent(Event.Dialog(DialogState.Select, DialogAction.Open)) },
                title = stringResource(id = R.string.join_company),
                description = stringResource(R.string.join_company_info),
                image = R.drawable.img_join_company
            )
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SelectCompanyDialog(
    modifier: Modifier = Modifier,
    userInfo: UserInfo,
    onSelect: () -> Unit,
    onClose: () -> Unit
) {
    FDialog(
        content = {
            // 등록된 회사가 존재하지 않을 때
            if (userInfo.companyId == -1L) {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.join_company_info_if_not_exists),
                        style = Typography.body1,
                        color = Main356DF8
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        text = stringResource(R.string.join_company_info_two_if_not_exists),
                        textAlign = TextAlign.Center,
                        style = Typography.body2
                    )
                }
            } else {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.join_company_info_if_exists),
                        style = Typography.body1,
                        color = Main356DF8
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    FRoundedArrowButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onSelect,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Main356DF8.copy(alpha = 0.2f),
                            contentColor = Font191919
                        ),
                        content = {
                            Text(
                                text = userInfo.companyName,
                                style = Typography.body3
                            )
                        }
                    )
                }
            }
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
