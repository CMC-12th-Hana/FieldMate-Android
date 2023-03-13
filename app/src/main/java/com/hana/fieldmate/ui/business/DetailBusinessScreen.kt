package com.hana.fieldmate.ui.business

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hana.fieldmate.FieldMateScreen
import com.hana.fieldmate.R
import com.hana.fieldmate.data.local.UserInfo
import com.hana.fieldmate.domain.model.MemberNameEntity
import com.hana.fieldmate.getShortenFormattedTime
import com.hana.fieldmate.ui.DialogAction
import com.hana.fieldmate.ui.DialogState
import com.hana.fieldmate.ui.Event
import com.hana.fieldmate.ui.component.*
import com.hana.fieldmate.ui.theme.*
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
    loadMembers: (Long) -> Unit,
    navController: NavController,
    selectedMemberList: List<MemberNameEntity>,
    selectMember: (MemberNameEntity) -> Unit,
    removeMember: (MemberNameEntity) -> Unit,
    updateMembersBtnOnClick: () -> Unit
) {
    val businessEntity = uiState.businessEntity

    var selectMemberDialogOpen by rememberSaveable { mutableStateOf(false) }

    if (selectMemberDialogOpen) SelectMemberScreen(
        companyMembers = uiState.memberNameEntityList,
        selectedMemberList = selectedMemberList,
        selectMember = selectMember,
        unselectMember = removeMember,
        onSelect = {
            updateMembersBtnOnClick()
            sendEvent(Event.Dialog(DialogState.Select, DialogAction.Close))
        },
        onClosed = { sendEvent(Event.Dialog(DialogState.Select, DialogAction.Close)) }
    )

    var deleteBusinessDialogOpen by rememberSaveable { mutableStateOf(false) }

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

    var errorDialogOpen by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf("") }

    if (errorDialogOpen) ErrorDialog(
        errorMessage = errorMessage,
        onClose = { sendEvent(Event.Dialog(DialogState.Error, DialogAction.Close)) }
    )

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
                } else if (event.dialog == DialogState.Select) {
                    selectMemberDialogOpen = event.action == DialogAction.Open
                    if (selectMemberDialogOpen) loadMembers(userInfo.companyId)
                } else if (event.dialog == DialogState.Error) {
                    errorDialogOpen = event.action == DialogAction.Open
                    if (errorDialogOpen) errorMessage = event.description
                }
            }
        }
    }

    Scaffold(
        topBar = {
            /* TODO: 기타 사업 처리 확정 되면 수정
            if (businessEntity.name == "기타") {
                FAppBarWithBackBtn(
                    title = stringResource(R.string.detail_etc),
                    backBtnOnClick = { navController.navigateUp() }
                )
            }
             */

            FAppBarWithDeleteBtn(
                title = stringResource(id = R.string.detail_business),
                backBtnOnClick = { navController.navigateUp() },
                deleteBtnOnClick = {
                    sendEvent(Event.Dialog(DialogState.Delete, DialogAction.Open))
                }
            )
        },
    ) { innerPadding ->
        Box(modifier = modifier.padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(30.dp))

                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
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
                            text = businessEntity.name,
                            style = Typography.title2
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Icon(
                            modifier = Modifier.clickable(
                                onClick = {
                                    navController.navigate("${FieldMateScreen.EditBusiness.name}/${businessEntity.id}")
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
                                    .padding(top = 3.dp, bottom = 3.dp, start = 8.dp, end = 8.dp),
                                text = stringResource(id = R.string.business_period),
                                style = Typography.body6,
                                color = Color.White
                            )

                            Text(
                                text = "${businessEntity.startDate.getShortenFormattedTime()}~${businessEntity.endDate.getShortenFormattedTime()}",
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
                                    .padding(top = 3.dp, bottom = 3.dp, start = 8.dp, end = 8.dp),
                                text = stringResource(id = R.string.profit),
                                style = Typography.body6,
                                color = Main356DF8
                            )

                            Text(
                                text = businessEntity.revenue,
                                style = Typography.body2
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    FRoundedArrowButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            sendEvent(
                                Event.Dialog(
                                    DialogState.Select,
                                    DialogAction.Open
                                )
                            )
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
                        number = businessEntity.memberEntities.size,
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    FRoundedArrowButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { navController.navigate(FieldMateScreen.VisitGraph.name) },
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
                        onClick = { navController.navigate("${FieldMateScreen.SummaryTask.name}/${businessEntity.id}") },
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
                        textStyle = TextStyle(
                            fontFamily = Pretendard,
                            color = Font70747E,
                            fontSize = 16.sp
                        ),
                        msgContent = businessEntity.description,
                        singleLine = false,
                        readOnly = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}
