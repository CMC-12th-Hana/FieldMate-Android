package com.hana.fieldmate.ui.business

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hana.fieldmate.FieldMateScreen
import com.hana.fieldmate.R
import com.hana.fieldmate.data.local.UserInfo
import com.hana.fieldmate.data.local.fakeBusinessDataSource
import com.hana.fieldmate.domain.model.BusinessEntity
import com.hana.fieldmate.getFormattedTime
import com.hana.fieldmate.getShortenFormattedTime
import com.hana.fieldmate.ui.DialogAction
import com.hana.fieldmate.ui.DialogState
import com.hana.fieldmate.ui.Event
import com.hana.fieldmate.ui.business.viewmodel.BusinessListUiState
import com.hana.fieldmate.ui.component.*
import com.hana.fieldmate.ui.theme.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BusinessScreen(
    modifier: Modifier = Modifier,
    eventsFlow: Flow<Event>,
    sendEvent: (Event) -> Unit,
    loadBusinesses: (Long, String?, String, String) -> Unit,
    uiState: BusinessListUiState,
    userInfo: UserInfo,
    navController: NavController
) {
    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded },
        skipHalfExpanded = true,
    )

    var selectionMode by rememberSaveable { mutableStateOf(DateSelectionMode.START) }

    var businessName by rememberSaveable { mutableStateOf("") }

    var selectedName: String? by rememberSaveable { mutableStateOf(null) }
    var selectedStartDate: LocalDate by rememberSaveable { mutableStateOf(LocalDate.now()) }
    var selectedEndDate: LocalDate by rememberSaveable { mutableStateOf(LocalDate.now()) }

    val selectedDate =
        if (selectionMode == DateSelectionMode.START) selectedStartDate else selectedEndDate

    var errorDialogOpen by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf("") }

    if (errorDialogOpen) ErrorDialog(
        errorMessage = errorMessage,
        onClose = { sendEvent(Event.Dialog(DialogState.Error, DialogAction.Close)) }
    )

    LaunchedEffect(selectedName, selectedStartDate, selectedEndDate) {
        loadBusinesses(
            userInfo.companyId,
            selectedName,
            selectedStartDate.getFormattedTime(),
            selectedEndDate.getFormattedTime()
        )
    }

    LaunchedEffect(true) {
        loadBusinesses(
            userInfo.companyId,
            null,
            selectedStartDate.getFormattedTime(),
            selectedEndDate.getFormattedTime()
        )

        eventsFlow.collectLatest { event ->
            when (event) {
                is Event.NavigateTo -> navController.navigate(event.destination)
                is Event.NavigatePopUpTo -> navController.navigate(event.destination) {
                    popUpTo(event.popUpDestination) {
                        inclusive = event.inclusive
                    }
                }
                is Event.NavigateUp -> navController.navigateUp()
                is Event.Dialog -> if (event.dialog == DialogState.Error) {
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
                    selectedDate = selectedDate,
                    startDate = if (selectionMode == DateSelectionMode.START) null else selectedStartDate,
                    endDate = if (selectionMode == DateSelectionMode.END) null else selectedEndDate,
                    onYearMonthChanged = { },
                    onDayClicked = {
                        if (selectionMode == DateSelectionMode.START) selectedStartDate =
                            it else selectedEndDate =
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
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp)) {
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
                                selectedDate = selectedEndDate,
                                calendarBtnOnClick = {
                                    selectionMode = DateSelectionMode.END
                                    coroutineScope.launch {
                                        modalSheetState.animateTo(ModalBottomSheetValue.Expanded)
                                    }
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }

                BusinessContent(
                    businessEntityList = uiState.businessList,
                    navController = navController
                )
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
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
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

            Spacer(modifier = Modifier.height(10.dp))
        }

        items(businessEntityList) { business ->
            BusinessItem(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    navController.navigate("${FieldMateScreen.DetailBusiness.name}/${business.id}")
                },
                businessEntity = business
            )
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BusinessItem(
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
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
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

@Preview
@Composable
fun PreviewBusinessItem() {
    FieldMateTheme {
        BusinessItem(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

            },
            businessEntity = fakeBusinessDataSource[0]
        )
    }
}