package com.hana.fieldmate.ui.client

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hana.fieldmate.FieldMateScreen
import com.hana.fieldmate.R
import com.hana.fieldmate.data.local.UserInfo
import com.hana.fieldmate.domain.model.BusinessEntity
import com.hana.fieldmate.domain.model.ClientEntity
import com.hana.fieldmate.toFormattedPhoneNum
import com.hana.fieldmate.ui.DialogAction
import com.hana.fieldmate.ui.DialogState
import com.hana.fieldmate.ui.Event
import com.hana.fieldmate.ui.business.BusinessItem
import com.hana.fieldmate.ui.client.viewmodel.ClientUiState
import com.hana.fieldmate.ui.component.*
import com.hana.fieldmate.ui.theme.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DetailClientScreen(
    modifier: Modifier = Modifier,
    uiState: ClientUiState,
    userInfo: UserInfo,
    eventsFlow: Flow<Event>,
    sendEvent: (Event) -> Unit,
    loadClient: () -> Unit,
    loadBusinessList: () -> Unit,
    deleteClient: () -> Unit,
    navController: NavController
) {
    val clientEntity = uiState.clientEntity
    val businessEntityList = uiState.businessEntityList

    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded },
        skipHalfExpanded = true
    )

    var selectionMode by rememberSaveable { mutableStateOf(DateSelectionMode.START) }

    var startDate: LocalDate? by rememberSaveable { mutableStateOf(null) }
    var endDate: LocalDate? by rememberSaveable { mutableStateOf(null) }

    val selectedDate = if (selectionMode == DateSelectionMode.START) startDate else endDate

    var businessKeyword by rememberSaveable { mutableStateOf("") }

    var deleteClientDialogOpen by rememberSaveable { mutableStateOf(false) }

    if (deleteClientDialogOpen) DeleteDialog(
        message = stringResource(id = R.string.delete_client_message),
        onClose = {
            sendEvent(Event.Dialog(DialogState.Delete, DialogAction.Close))
        },
        onConfirm = {
            deleteClient()
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
        loadClient()
        loadBusinessList()

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
                    deleteClientDialogOpen = event.action == DialogAction.Open
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
                if (userInfo.userRole == "리더") {
                    FAppBarWithDeleteBtn(
                        title = stringResource(id = R.string.detail_client),
                        backBtnOnClick = {
                            navController.navigateUp()
                        },
                        deleteBtnOnClick = {
                            sendEvent(Event.Dialog(DialogState.Delete, DialogAction.Open))
                        }
                    )
                } else {
                    FAppBarWithBackBtn(
                        title = stringResource(id = R.string.detail_client),
                        backBtnOnClick = { navController.navigateUp() }
                    )
                }
            },
        ) { innerPadding ->
            Box(modifier = modifier.padding(innerPadding)) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Spacer(modifier = Modifier.height(30.dp))

                        DetailClientContent(
                            clientEntity = clientEntity,
                            editBtnOnClick = { navController.navigate("${FieldMateScreen.EditClient}/${clientEntity.id}") },
                            taskGraphBtnOnClick = { navController.navigate("${FieldMateScreen.TaskGraph}/${clientEntity.id}") }
                        )

                        Spacer(modifier = Modifier.height(50.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = stringResource(id = R.string.search_business),
                                style = Typography.title2
                            )
                        }

                        Spacer(modifier = Modifier.height(30.dp))

                        Column(modifier = Modifier.fillMaxWidth()) {
                            FSearchTextField(
                                modifier = Modifier.fillMaxWidth(),
                                msgContent = businessKeyword,
                                hint = stringResource(id = R.string.search_business_hint),
                                onValueChange = { businessKeyword = it }
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
                        }

                    }

                    BusinessContent(
                        businessEntityList = businessEntityList,
                        clientId = clientEntity.id,
                        navController = navController
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DetailClientContent(
    modifier: Modifier = Modifier,
    taskGraphBtnOnClick: () -> Unit,
    editBtnOnClick: () -> Unit,
    clientEntity: ClientEntity
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

        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = clientEntity.name,
                    style = Typography.title2
                )

                Spacer(modifier = Modifier.width(4.dp))

                Icon(
                    modifier = Modifier.clickable(
                        onClick = editBtnOnClick
                    ),
                    painter = painterResource(id = R.drawable.ic_gray_edit),
                    tint = Color.Unspecified,
                    contentDescription = null
                )
            }

            if (clientEntity.salesRepresentativeDepartment != "") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.department_in_charge),
                        style = Typography.body3
                    )

                    Spacer(
                        modifier = Modifier
                            .width(1.dp)
                            .height(12.dp)
                            .background(color = LineDBDBDB, shape = RectangleShape)
                    )

                    Text(
                        text = clientEntity.salesRepresentativeDepartment,
                        style = Typography.body3
                    )
                }
            }
        }

    }

    Spacer(modifier = Modifier.height(30.dp))

    PhoneItem(
        modifier = Modifier.fillMaxWidth(),
        name = stringResource(id = R.string.client_phone),
        phone = clientEntity.phone
    )

    Spacer(modifier = Modifier.height(10.dp))

    if (clientEntity.salesRepresentativeName != "" && clientEntity.salesRepresentativePhone != "") {
        PhoneItem(
            modifier = Modifier.fillMaxWidth(),
            name = clientEntity.salesRepresentativeName,
            phone = clientEntity.salesRepresentativePhone
        )
    }

    Spacer(modifier = Modifier.height(70.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${clientEntity.name}에서 함께한 사업",
            style = Typography.title2
        )

        Spacer(modifier = Modifier.width(4.dp))

        Icon(
            painter = painterResource(id = R.drawable.ic_info),
            tint = Color.Unspecified,
            contentDescription = null
        )
    }

    Spacer(modifier = Modifier.height(30.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Surface(
            onClick = taskGraphBtnOnClick,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .weight(1f),
            shape = Shapes.large,
            border = BorderStroke(1.dp, LineDBDBDB),
            color = Color.White
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.total_work_number),
                        style = Typography.body1
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Icon(
                        painter = painterResource(id = R.drawable.ic_graph),
                        tint = Color.Unspecified,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "${clientEntity.taskCount}",
                        style = Typography.title1,
                        color = Main356DF8,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .weight(1f),
            shape = Shapes.large,
            border = BorderStroke(width = 1.dp, color = LineDBDBDB),
            color = BgF8F8FA
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.business_number),
                        style = Typography.body1
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Icon(
                        painter = painterResource(id = R.drawable.ic_business),
                        tint = Color.Unspecified,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "${clientEntity.businessCount}",
                        style = Typography.title1,
                        color = Main356DF8,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

fun LazyListScope.BusinessContent(
    businessEntityList: List<BusinessEntity>,
    clientId: Long,
    navController: NavController
) {
    item {
        FAddButton(
            onClick = { navController.navigate("${FieldMateScreen.AddBusiness.name}/$clientId") },
            text = stringResource(id = R.string.add_business),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))
    }

    item {
        FRoundedArrowButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { /*TODO*/ },
            contentPadding = PaddingValues(top = 24.dp, bottom = 24.dp, start = 20.dp, end = 15.dp),
            content = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(id = R.string.etc_business),
                        style = Typography.body1
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Icon(
                        modifier = Modifier.size(18.dp),
                        painter = painterResource(id = R.drawable.ic_info),
                        tint = Color.Unspecified,
                        contentDescription = null
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(10.dp))
    }

    items(businessEntityList) { business ->
        BusinessItem(
            modifier = Modifier.fillMaxWidth(),
            onClick = { navController.navigate("${FieldMateScreen.DetailBusiness.name}/${business.id}") },
            businessEntity = business
        )

        Spacer(modifier = Modifier.height(10.dp))
    }

    item {
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PhoneItem(
    modifier: Modifier = Modifier,
    name: String,
    phone: String
) {
    Surface(
        modifier = modifier,
        shape = Shapes.large,
        color = BgF1F1F5,
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp, bottom = 15.dp, start = 20.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Text(text = name, style = Typography.body3)

                Spacer(modifier = Modifier.width(15.dp))

                Text(text = phone, style = Typography.body3)
            }

            val context = LocalContext.current

            CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                IconButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse(phone.toFormattedPhoneNum())
                        }
                        context.startActivity(intent)
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_call),
                        tint = Color.Unspecified,
                        contentDescription = null
                    )
                }
            }
        }
    }
}
