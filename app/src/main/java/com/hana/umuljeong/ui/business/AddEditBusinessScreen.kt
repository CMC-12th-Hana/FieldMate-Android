package com.hana.umuljeong.ui.business

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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hana.umuljeong.EditMode
import com.hana.umuljeong.R
import com.hana.umuljeong.domain.Member
import com.hana.umuljeong.ui.auth.Label
import com.hana.umuljeong.ui.component.*
import com.hana.umuljeong.ui.theme.*
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddEditBusinessScreen(
    modifier: Modifier = Modifier,
    mode: EditMode,
    uiState: BusinessUiState,
    selectedMemberList: List<Member>,
    navController: NavController,
    addMemberBtnOnClick: (List<Member>) -> Unit,
    removeMember: (Member) -> Unit,
    confirmBtnOnClick: () -> Unit
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

    val selectedDate = if (selectionMode == DateSelectionMode.START) startDate else endDate

    var name by rememberSaveable { mutableStateOf(business.name) }
    var content by rememberSaveable { mutableStateOf(business.content) }
    var profit by rememberSaveable { mutableStateOf(business.profit) }

    var selectMemberDialogOpen by rememberSaveable { mutableStateOf(false) }

    if (selectMemberDialogOpen) SelectMemberDialog(
        onSelected = { members ->
            addMemberBtnOnClick(members)
            selectMemberDialogOpen = false
        },
        onClosed = { selectMemberDialogOpen = false }
    )

    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetShape = RoundedCornerShape(
            topStart = 12.dp,
            topEnd = 12.dp
        ),
        sheetBackgroundColor = Color.White,
        sheetContent = {
            Column(modifier = Modifier.fillMaxWidth()) {
                UDatePicker(
                    modifier = Modifier.padding(40.dp),
                    selectedDate = selectedDate ?: LocalDate.now(),
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
        Scaffold(
            topBar = {
                UAppBarWithBackBtn(
                    title = stringResource(id = if (mode == EditMode.Add) R.string.add_business else R.string.edit_business),
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
                        .padding(start = 20.dp, end = 20.dp)
                        .verticalScroll(rememberScrollState())
                        .weight(1f)
                ) {
                    Spacer(modifier = Modifier.height(30.dp))

                    Label(text = stringResource(id = R.string.business_name))
                    Spacer(modifier = Modifier.height(8.dp))
                    UTextField(
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
                        UDateField(
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
                        UDateField(
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

                    Label(text = stringResource(id = R.string.add_members))
                    Spacer(modifier = Modifier.height(8.dp))
                    URoundedArrowButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { selectMemberDialogOpen = true },
                        text = stringResource(id = R.string.get_member_profile),
                        icon = painterResource(id = R.drawable.ic_member_profile)
                    )

                    Column(modifier = Modifier.fillMaxWidth()) {
                        if (selectedMemberList.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(20.dp))

                            for (member in selectedMemberList) {
                                DeletableMemberItem(
                                    onClick = { removeMember(member) },
                                    member = member
                                )

                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.remark),
                        style = Typography.body3
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    UTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 130.dp, max = Dp.Infinity),
                        textStyle = TextStyle(
                            fontFamily = Pretendard,
                            color = Font70747E,
                            fontSize = 16.sp
                        ),
                        msgContent = content,
                        onValueChange = { content = it },
                        singleLine = false,
                        readOnly = true
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(text = stringResource(id = R.string.profit), style = Typography.body4)
                    Spacer(modifier = Modifier.height(8.dp))
                    UTextField(
                        modifier = Modifier.fillMaxWidth(),
                        msgContent = profit,
                        hint = stringResource(R.string.profit_hint),
                        onValueChange = { profit = it }
                    )

                    Spacer(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                    )

                    Column {
                        Spacer(Modifier.height(40.dp))

                        UButton(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(id = R.string.complete),
                            onClick = confirmBtnOnClick
                        )

                        Spacer(Modifier.height(50.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DeletableMemberItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    shape: Shape = Shapes.large,
    member: Member
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
                .padding(top = 15.dp, bottom = 15.dp, start = 20.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    modifier = Modifier.size(40.dp),
                    painter = painterResource(id = member.profileImg),
                    contentDescription = null,
                    tint = Color.Unspecified
                )

                Text(text = member.name, style = Typography.body2)
            }

            CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                IconButton(onClick = onClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_remove),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                }
            }

        }
    }
}