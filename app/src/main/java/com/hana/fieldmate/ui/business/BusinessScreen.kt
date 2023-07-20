package com.hana.fieldmate.ui.business

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.hana.fieldmate.App
import com.hana.fieldmate.R
import com.hana.fieldmate.data.ErrorType
import com.hana.fieldmate.domain.model.BusinessEntity
import com.hana.fieldmate.ui.DialogEvent
import com.hana.fieldmate.ui.business.viewmodel.BusinessListViewModel
import com.hana.fieldmate.ui.component.*
import com.hana.fieldmate.ui.navigation.FieldMateScreen
import com.hana.fieldmate.ui.theme.*
import com.hana.fieldmate.util.DateUtil.getFormattedTime
import com.hana.fieldmate.util.DateUtil.getShortenFormattedTime
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BusinessScreen(
    modifier: Modifier = Modifier,
    viewModel: BusinessListViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val userInfo = App.getInstance().getUserInfo()

    when (uiState.dialog) {
        is DialogEvent.Error -> {
            when (val error = (uiState.dialog as DialogEvent.Error).errorType) {
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

    var businessName by remember { mutableStateOf("") }

    var selectedName: String? by remember { mutableStateOf(null) }
    var selectedStartDate: LocalDate? by remember { mutableStateOf(null) }
    var selectedEndDate: LocalDate? by remember { mutableStateOf(null) }

    val selectedDate = if (selectionMode == DateSelectionMode.START) {
        selectedStartDate
    } else {
        selectedEndDate
    }

    LaunchedEffect(selectedName, selectedStartDate, selectedEndDate) {
        viewModel.loadBusinesses(
            userInfo.companyId,
            selectedName,
            selectedStartDate?.getFormattedTime(),
            selectedEndDate?.getFormattedTime()
        )
    }

    LaunchedEffect(true) {
        viewModel.loadBusinesses(
            userInfo.companyId,
            null,
            null,
            null
        )
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
                    selectedDate = selectedDate,
                    startDate = if (selectionMode == DateSelectionMode.START) null else selectedStartDate,
                    endDate = if (selectionMode == DateSelectionMode.END) null else selectedEndDate,
                    onYearMonthChanged = { },
                    onDayClicked = {
                        if (selectionMode == DateSelectionMode.START) {
                            selectedStartDate = it
                        } else {
                            selectedEndDate = it
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
            bottomBar = {
                FBottomBar(
                    navController = navController
                )
            }
        ) { innerPadding ->
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, LineDBDBDB),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp)
                    ) {
                        Spacer(modifier = Modifier.height(20.dp))

                        FSearchTextField(
                            modifier = Modifier.fillMaxWidth(),
                            msgContent = businessName,
                            hint = stringResource(id = R.string.search_business_hint),
                            onSearch = { selectedName = it },
                            onValueChange = { businessName = it }
                        )

                        Spacer(modifier = Modifier.height(10.dp))

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
                                selectedDate = selectedStartDate,
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
                                selectedDate = selectedEndDate,
                                calendarBtnOnClick = {
                                    selectionMode = DateSelectionMode.END
                                    coroutineScope.launch {
                                        modalSheetState.show()
                                    }
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }

                LoadingContent(loadingState = uiState.businessListLoadingState) {
                    BusinessContent(
                        businessEntityList = uiState.businessList.filter { it.name != "기타" },
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun BusinessContent(
    modifier: Modifier = Modifier,
    businessEntityList: List<BusinessEntity>,
    navController: NavController
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(start = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.ic_business),
                tint = Color.Unspecified,
                contentDescription = null
            )

            Text(text = stringResource(id = R.string.my_business), style = Typography.body2)
        }

        Spacer(modifier = Modifier.height(20.dp))

        for (business in businessEntityList) {
            BusinessItemWithClientName(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    navController.navigate("${FieldMateScreen.DetailBusiness.name}/${business.id}")
                },
                businessEntity = business
            )

            Spacer(modifier = Modifier.height(10.dp))
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BusinessItemWithClientName(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    shape: Shape = Shapes.large,
    businessEntity: BusinessEntity
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        color = BgF8F8FA,
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 20.dp, start = 15.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Text(
                        text = businessEntity.clientName,
                        style = Typography.body3,
                        color = Font70747E
                    )
                    Text(text = businessEntity.name, style = Typography.body2)

                    Spacer(modifier = Modifier.height(10.dp))

                    Row {
                        Text(
                            text = stringResource(id = R.string.business_period),
                            style = Typography.body3,
                            color = Font70747E
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = "${businessEntity.startDate.getShortenFormattedTime()} - ${businessEntity.endDate.getShortenFormattedTime()}",
                            style = Typography.body3
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_circle_member),
                        tint = Color.Unspecified,
                        contentDescription = null
                    )

                    Text(
                        text = "${businessEntity.memberEntities.size}",
                        style = Typography.body2
                    )
                }
            }
        }
    }
}
