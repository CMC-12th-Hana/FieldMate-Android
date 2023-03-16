package com.hana.fieldmate.ui.business

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hana.fieldmate.EditMode
import com.hana.fieldmate.R
import com.hana.fieldmate.data.local.UserInfo
import com.hana.fieldmate.domain.model.MemberNameEntity
import com.hana.fieldmate.ui.DialogAction
import com.hana.fieldmate.ui.DialogState
import com.hana.fieldmate.ui.Event
import com.hana.fieldmate.ui.auth.Label
import com.hana.fieldmate.ui.business.viewmodel.BusinessUiState
import com.hana.fieldmate.ui.component.*
import com.hana.fieldmate.ui.theme.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddEditBusinessScreen(
    modifier: Modifier = Modifier,
    eventsFlow: Flow<Event>,
    sendEvent: (Event) -> Unit,
    loadBusiness: () -> Unit,
    userInfo: UserInfo,
    loadCompanyMembers: (Long, String?) -> Unit,
    mode: EditMode,
    uiState: BusinessUiState,
    navController: NavController,
    selectedMemberList: List<MemberNameEntity>,
    selectMember: (MemberNameEntity) -> Unit,
    removeMember: (MemberNameEntity) -> Unit,
    confirmBtnOnClick: (String, LocalDate, LocalDate, Long, String) -> Unit
) {
    val business = uiState.business

    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded },
        skipHalfExpanded = true,
    )

    var selectionMode by rememberSaveable { mutableStateOf(DateSelectionMode.START) }

    var startDate: LocalDate? by rememberSaveable { mutableStateOf(null) }
    var endDate: LocalDate? by rememberSaveable { mutableStateOf(null) }

    var name by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var revenue by rememberSaveable { mutableStateOf("") }

    val selectedDate = if (selectionMode == DateSelectionMode.START) startDate else endDate

    var selectMemberDialogOpen by rememberSaveable { mutableStateOf(false) }

    if (selectMemberDialogOpen && mode == EditMode.Add) AddBusinessMemberDialog(
        companyMembers = uiState.memberNameList,
        selectedMemberList = selectedMemberList,
        selectMember = selectMember,
        unselectMember = removeMember,
        onSearch = { loadCompanyMembers(userInfo.companyId, it) },
        onSelect = { sendEvent(Event.Dialog(DialogState.Select, DialogAction.Close)) },
        onClosed = { sendEvent(Event.Dialog(DialogState.Select, DialogAction.Close)) }
    )

    var errorDialogOpen by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf("") }

    if (errorDialogOpen) ErrorDialog(
        errorMessage = errorMessage,
        onClose = { sendEvent(Event.Dialog(DialogState.Error, DialogAction.Close)) }
    )

    LaunchedEffect(business) {
        name = business.name
        description = business.description
        revenue = business.revenue
        startDate = business.startDate
        endDate = business.endDate
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
                is Event.Dialog -> if (event.dialog == DialogState.Select) {
                    selectMemberDialogOpen = event.action == DialogAction.Open
                    if (selectMemberDialogOpen) loadCompanyMembers(userInfo.companyId, null)
                } else if (event.dialog == DialogState.Error) {
                    errorDialogOpen = event.action == DialogAction.Open
                    if (errorDialogOpen) errorMessage = event.description
                }
            }
        }
    }

    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetShape = RoundedCornerShape(
            topStart = 12.dp,
            topEnd = 12.dp
        ),
        sheetBackgroundColor = Color.White,
        sheetContent = {
            Column(modifier = Modifier.fillMaxWidth()) {
                DatePicker(
                    modifier = Modifier.padding(40.dp),
                    selectedDate = selectedDate ?: LocalDate.now(),
                    startDate = if (selectionMode == DateSelectionMode.START) null else startDate,
                    endDate = if (selectionMode == DateSelectionMode.END) null else endDate,
                    onYearMonthChanged = { },
                    onDayClicked = {
                        if (selectionMode == DateSelectionMode.START) startDate = it else endDate =
                            it
                        coroutineScope.launch {
                            modalSheetState.animateTo(ModalBottomSheetValue.Hidden)
                        }
                    }
                )
            }
        }
    ) {
        BackHandler(enabled = modalSheetState.isVisible) {
            coroutineScope.launch {
                modalSheetState.hide()
            }
        }

        Scaffold(
            topBar = {
                FAppBarWithBackBtn(
                    title = stringResource(id = if (mode == EditMode.Add) R.string.add_business else R.string.edit_business),
                    backBtnOnClick = {
                        navController.navigateUp()
                    }
                )
            },
        ) { innerPadding ->
            LoadingContent(loadingState = uiState.businessLoadingState) {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp)
                            .verticalScroll(rememberScrollState())
                            .weight(1f)
                    ) {
                        Spacer(modifier = Modifier.height(30.dp))

                        Label(text = stringResource(id = R.string.business_name))
                        Spacer(modifier = Modifier.height(8.dp))
                        FTextField(
                            modifier = Modifier.fillMaxWidth(),
                            msgContent = name,
                            hint = stringResource(R.string.business_name_hint),
                            onValueChange = { name = it }
                        )

                        Spacer(modifier = Modifier.height(30.dp))

                        Label(text = stringResource(id = R.string.expected_business_period))
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            DateField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                hint = stringResource(id = R.string.start_date_hint),
                                selectedDate = startDate,
                                calendarBtnOnClick = {
                                    selectionMode = DateSelectionMode.START
                                    coroutineScope.launch {
                                        modalSheetState.animateTo(ModalBottomSheetValue.Expanded)
                                    }
                                }
                            )
                            Spacer(
                                modifier = Modifier
                                    .width(10.dp)
                                    .height(1.dp)
                                    .background(FontDBDBDB)
                            )
                            DateField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                hint = stringResource(id = R.string.end_date_hint),
                                selectedDate = endDate,
                                calendarBtnOnClick = {
                                    selectionMode = DateSelectionMode.END
                                    coroutineScope.launch {
                                        modalSheetState.animateTo(ModalBottomSheetValue.Expanded)
                                    }
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(30.dp))

                        if (mode == EditMode.Add) {
                            Label(text = stringResource(id = R.string.add_members))
                            Spacer(modifier = Modifier.height(8.dp))
                            FRoundedArrowButton(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    sendEvent(Event.Dialog(DialogState.Select, DialogAction.Open))
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
                                            text = stringResource(id = R.string.get_member_profile),
                                            style = Typography.body2
                                        )
                                    }
                                }
                            )

                            Spacer(modifier = Modifier.height(30.dp))
                        }

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
                            msgContent = description,
                            onValueChange = { description = it },
                            singleLine = false
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(text = stringResource(id = R.string.profit), style = Typography.body4)
                        Spacer(modifier = Modifier.height(8.dp))
                        FTextField(
                            modifier = Modifier.fillMaxWidth(),
                            msgContent = revenue,
                            hint = stringResource(R.string.profit_hint),
                            onValueChange = { revenue = it }
                        )

                        Spacer(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f)
                        )
                    }

                    Column {
                        Spacer(Modifier.height(40.dp))

                        FButton(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(id = R.string.complete),
                            onClick = {
                                confirmBtnOnClick(
                                    name,
                                    startDate!!,
                                    endDate!!,
                                    if (revenue == "") 0L else revenue.toLong(),
                                    description
                                )
                            }
                        )

                        Spacer(Modifier.height(50.dp))
                    }
                }
            }
        }
    }
}