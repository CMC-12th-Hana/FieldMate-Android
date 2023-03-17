package com.hana.fieldmate.ui.business

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hana.fieldmate.FieldMateScreen
import com.hana.fieldmate.R
import com.hana.fieldmate.data.local.UserInfo
import com.hana.fieldmate.ui.DialogAction
import com.hana.fieldmate.ui.DialogState
import com.hana.fieldmate.ui.Event
import com.hana.fieldmate.ui.business.viewmodel.BusinessUiState
import com.hana.fieldmate.ui.component.*
import com.hana.fieldmate.ui.theme.*
import com.hana.fieldmate.util.DateUtil.getShortenFormattedTime
import com.hana.fieldmate.util.LEADER
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DetailBusinessScreen(
    modifier: Modifier = Modifier,
    uiState: BusinessUiState,
    userInfo: UserInfo,
    eventsFlow: Flow<Event>,
    sendEvent: (Event) -> Unit,
    loadBusiness: () -> Unit,
    deleteBusiness: () -> Unit,
    navController: NavController
) {
    val business = uiState.business

    var deleteBusinessDialogOpen by remember { mutableStateOf(false) }
    if (deleteBusinessDialogOpen) DeleteDialog(
        message = stringResource(id = R.string.delete_business_message),
        onClose = {
            sendEvent(Event.Dialog(DialogState.Delete, DialogAction.Close))
        },
        onConfirm = {
            deleteBusiness()
            sendEvent(Event.Dialog(DialogState.Delete, DialogAction.Close))
        }
    )

    var errorDialogOpen by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    if (errorDialogOpen) ErrorDialog(
        errorMessage = errorMessage,
        onClose = { sendEvent(Event.Dialog(DialogState.Error, DialogAction.Close)) }
    )

    var jwtExpiredDialogOpen by remember { mutableStateOf(false) }
    if (jwtExpiredDialogOpen) JwtExpiredDialog(sendEvent = sendEvent)

    LaunchedEffect(true) {
        loadBusiness()

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
                    deleteBusinessDialogOpen = event.action == DialogAction.Open
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
            if (userInfo.userRole == LEADER) {
                FAppBarWithDeleteBtn(
                    title = stringResource(id = R.string.detail_business),
                    backBtnOnClick = { navController.navigateUp() },
                    deleteBtnOnClick = {
                        sendEvent(Event.Dialog(DialogState.Delete, DialogAction.Open))
                    }
                )
            } else {
                FAppBarWithBackBtn(
                    title = stringResource(id = R.string.detail_business),
                    backBtnOnClick = { navController.navigateUp() }
                )
            }
        },
    ) { innerPadding ->
        Box(modifier = modifier.padding(innerPadding)) {
            LoadingContent(loadingState = uiState.businessLoadingState) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(start = 20.dp, end = 20.dp)
                ) {
                    Spacer(modifier = Modifier.height(30.dp))

                    Column(modifier = modifier.fillMaxWidth()) {
                        Row(
                            modifier = modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(BgF1F1F5),
                                painter = painterResource(id = R.drawable.ic_company),
                                tint = Color.Unspecified,
                                contentDescription = null
                            )

                            Spacer(modifier = Modifier.width(15.dp))

                            Text(
                                text = business.name,
                                style = Typography.title2
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Icon(
                                modifier = Modifier.clickable(
                                    onClick = {
                                        navController.navigate("${FieldMateScreen.EditBusiness.name}/${business.id}")
                                    }
                                ),
                                painter = painterResource(id = R.drawable.ic_gray_edit),
                                tint = Color.Unspecified,
                                contentDescription = null
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Column(
                            modifier = Modifier.padding(start = 10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    modifier = Modifier
                                        .background(
                                            color = Main356DF8,
                                            shape = MaterialTheme.shapes.small
                                        )
                                        .padding(
                                            top = 3.dp,
                                            bottom = 3.dp,
                                            start = 8.dp,
                                            end = 8.dp
                                        ),
                                    text = stringResource(id = R.string.business_period),
                                    style = Typography.body6,
                                    color = Color.White
                                )

                                Text(
                                    text = "${business.startDate.getShortenFormattedTime()}~${business.endDate.getShortenFormattedTime()}",
                                    style = Typography.body2
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    modifier = Modifier
                                        .background(
                                            color = Color.Transparent,
                                            shape = MaterialTheme.shapes.small
                                        )
                                        .border(width = 1.dp, color = Color(0xFFBECCE9))
                                        .padding(
                                            top = 3.dp,
                                            bottom = 3.dp,
                                            start = 8.dp,
                                            end = 8.dp
                                        ),
                                    text = stringResource(id = R.string.profit),
                                    style = Typography.body6,
                                    color = Main356DF8
                                )

                                Text(
                                    text = business.revenue,
                                    style = Typography.body2
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(30.dp))

                        FRoundedArrowButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                navController.navigate("${FieldMateScreen.BusinessMember.name}/${business.id}")
                            },
                            content = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        modifier = Modifier.size(40.dp),
                                        painter = painterResource(id = R.drawable.ic_member_profile),
                                        tint = Color.Unspecified,
                                        contentDescription = null
                                    )

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Text(
                                        text = stringResource(id = R.string.business_members),
                                        style = Typography.body2
                                    )
                                }
                            },
                            number = business.memberEntities.size,
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        FRoundedArrowButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { navController.navigate("${FieldMateScreen.BusinessTaskGraph.name}/${business.id}") },
                            content = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        modifier = Modifier.size(40.dp),
                                        painter = painterResource(id = R.drawable.ic_graph),
                                        tint = Color.Unspecified,
                                        contentDescription = null
                                    )

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Text(
                                        text = stringResource(id = R.string.work_graph),
                                        style = Typography.body2
                                    )
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        FRoundedArrowButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { navController.navigate("${FieldMateScreen.SummaryTask.name}/${business.id}") },
                            content = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        modifier = Modifier.size(40.dp),
                                        painter = painterResource(id = R.drawable.ic_calendar),
                                        tint = Color.Unspecified,
                                        contentDescription = null
                                    )

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Text(
                                        text = stringResource(id = R.string.task_by_day),
                                        style = Typography.body2
                                    )
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(30.dp))

                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(id = R.string.remark),
                            style = Typography.body3
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        FTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 130.dp, max = Dp.Infinity),
                            msgContent = business.description,
                            singleLine = false,
                            readOnly = true
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}
