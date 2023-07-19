package com.hana.fieldmate.ui.business

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hana.fieldmate.App
import com.hana.fieldmate.R
import com.hana.fieldmate.data.ErrorType
import com.hana.fieldmate.ui.DialogType
import com.hana.fieldmate.ui.auth.Label
import com.hana.fieldmate.ui.business.viewmodel.BusinessViewModel
import com.hana.fieldmate.ui.component.*
import com.hana.fieldmate.ui.navigation.EditMode
import com.hana.fieldmate.ui.navigation.NavigateActions
import com.hana.fieldmate.ui.theme.*
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddEditBusinessScreen(
    modifier: Modifier = Modifier,
    viewModel: BusinessViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val userInfo = App.getInstance().getUserInfo()
    val business = uiState.business

    when (uiState.dialog) {
        is DialogType.Select -> {
            AddBusinessMemberDialog(
                companyMembers = uiState.memberNameList,
                selectedMemberList = viewModel.selectedMemberList,
                selectMember = viewModel::selectMember,
                unselectMember = viewModel::removeMember,
                onSearch = { viewModel.loadCompanyMembers(userInfo.companyId, it) },
                onSelect = { viewModel.onDialogClosed() },
                onClosed = { viewModel.onDialogClosed() }
            )
        }
        is DialogType.Error -> {
            when (val error = (uiState.dialog as DialogType.Error).errorType) {
                is ErrorType.JwtExpired -> {
                    BackToLoginDialog(onClose = { viewModel.backToLogin() })
                }
                is ErrorType.General -> {
                    ErrorDialog(
                        errorMessage = error.errorMessage,
                        onClose = { viewModel.onDialogClosed() }
                    )
                }
            }
        }
        else -> {}
    }

    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded },
        skipHalfExpanded = true
    )

    var selectionMode by remember { mutableStateOf(DateSelectionMode.START) }

    var startDate: LocalDate? by remember { mutableStateOf(null) }
    var endDate: LocalDate? by remember { mutableStateOf(null) }

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var revenue by remember { mutableStateOf("") }

    val selectedDate = if (selectionMode == DateSelectionMode.START) startDate else endDate

    LaunchedEffect(business) {
        name = business.name
        description = business.description
        revenue = business.revenue.toString()
        startDate = business.startDate
        endDate = business.endDate
    }

    LaunchedEffect(true) {
        viewModel.loadBusiness()
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
                        if (selectionMode == DateSelectionMode.START) {
                            startDate = it
                        } else {
                            endDate = it
                        }
                        coroutineScope.launch {
                            modalSheetState.hide()
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
                    title = stringResource(id = if (uiState.mode == EditMode.Add) R.string.add_business else R.string.edit_business),
                    backBtnOnClick = {
                        viewModel.navigateTo(NavigateActions.navigateUp())
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
                                        modalSheetState.show()
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
                                        modalSheetState.show()
                                    }
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(30.dp))

                        if (uiState.mode == EditMode.Add) {
                            Label(text = stringResource(id = R.string.add_members))
                            Spacer(modifier = Modifier.height(8.dp))
                            FRoundedArrowButton(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    viewModel.onDialogClosed()
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
                            hint = stringResource(id = R.string.remark_hint),
                            msgContent = description,
                            maxChar = 300,
                            onValueChange = { description = it },
                            singleLine = false
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(text = stringResource(id = R.string.profit), style = Typography.body4)
                        Spacer(modifier = Modifier.height(8.dp))
                        FTextField(
                            modifier = Modifier.fillMaxWidth(),
                            msgContent = revenue,
                            maxChar = 18,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            hint = stringResource(R.string.profit_hint),
                            onValueChange = {
                                revenue = it
                            }
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
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, end = 20.dp),
                            text = stringResource(id = R.string.complete),
                            onClick = {
                                if (uiState.mode == EditMode.Add) {
                                    viewModel.createBusiness(
                                        name,
                                        startDate!!,
                                        endDate!!,
                                        if (revenue == "") 0L else revenue.toLong(),
                                        description
                                    )
                                } else {
                                    viewModel.updateBusiness(
                                        name,
                                        startDate!!,
                                        endDate!!,
                                        if (revenue == "") 0L else revenue.toLong(),
                                        description
                                    )
                                }
                            }
                        )

                        Spacer(Modifier.height(50.dp))
                    }
                }
            }
        }
    }
}