package com.hana.fieldmate.ui.client

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hana.fieldmate.*
import com.hana.fieldmate.R
import com.hana.fieldmate.data.ErrorType
import com.hana.fieldmate.domain.model.BusinessEntity
import com.hana.fieldmate.domain.model.ClientEntity
import com.hana.fieldmate.ui.DialogType
import com.hana.fieldmate.ui.client.viewmodel.ClientViewModel
import com.hana.fieldmate.ui.component.*
import com.hana.fieldmate.ui.navigation.NavigateActions
import com.hana.fieldmate.ui.theme.*
import com.hana.fieldmate.util.DateUtil.getFormattedTime
import com.hana.fieldmate.util.DateUtil.getShortenFormattedTime
import com.hana.fieldmate.util.LEADER
import com.hana.fieldmate.util.StringUtil.toFormattedPhoneNum
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DetailClientScreen(
    modifier: Modifier = Modifier,
    viewModel: ClientViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val userInfo = App.getInstance().getUserInfo()
    val client = uiState.client
    val businessEntityList = uiState.businessList

    when (uiState.dialog) {
        is DialogType.Delete -> {
            DeleteDialog(
                message = stringResource(id = R.string.delete_client_message),
                onClose = {
                    viewModel.onDialogClosed()
                },
                onConfirm = {
                    viewModel.deleteClient()
                    viewModel.onDialogClosed()
                }
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

    var businessName by remember { mutableStateOf("") }

    var selectedName: String? by remember { mutableStateOf(null) }
    var selectedStartDate: LocalDate? by remember { mutableStateOf(null) }
    var selectedEndDate: LocalDate? by remember { mutableStateOf(null) }

    val selectedDate =
        if (selectionMode == DateSelectionMode.START) selectedStartDate else selectedEndDate

    var graphHintPopupOpen by remember { mutableStateOf(false) }
    var etcBusinessPopupOpen by remember { mutableStateOf(false) }

    LaunchedEffect(selectedName, selectedStartDate, selectedEndDate) {
        viewModel.loadBusinesses(
            selectedName,
            selectedStartDate?.getFormattedTime(),
            selectedEndDate?.getFormattedTime()
        )
    }

    LaunchedEffect(true) {
        viewModel.loadClient()
        viewModel.loadBusinesses(null, null, null)
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
                    startDate = if (selectionMode == DateSelectionMode.START) null else selectedStartDate,
                    endDate = if (selectionMode == DateSelectionMode.END) null else selectedEndDate,
                    onYearMonthChanged = { },
                    onDayClicked = {
                        if (selectionMode == DateSelectionMode.START) selectedStartDate =
                            it else selectedEndDate =
                            it
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
                if (userInfo.userRole == LEADER) {
                    FAppBarWithDeleteBtn(
                        title = stringResource(id = R.string.detail_client),
                        backBtnOnClick = {
                            viewModel.navigateTo(NavigateActions.navigateUp())
                        },
                        deleteBtnOnClick = {
                            viewModel.openDeleteDialog()
                        }
                    )
                } else {
                    FAppBarWithBackBtn(
                        title = stringResource(id = R.string.detail_client),
                        backBtnOnClick = {
                            viewModel.navigateTo(NavigateActions.navigateUp())
                        }
                    )
                }
            },
        ) { innerPadding ->
            Box(modifier = modifier.padding(innerPadding)) {
                LoadingContent(loadingState = uiState.clientLoadingState) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(30.dp))

                            DetailClientContent(
                                clientEntity = client,
                                editBtnOnClick = {
                                    viewModel.navigateTo(
                                        NavigateActions.DetailClientScreen
                                            .toEditClientScreen(client.id)
                                    )
                                },
                                graphHintPopupOpen = graphHintPopupOpen,
                                graphHintBtnOnClick = { graphHintPopupOpen = !graphHintPopupOpen },
                                taskGraphBtnOnClick = {
                                    viewModel.navigateTo(
                                        NavigateActions.DetailClientScreen
                                            .toClientTaskGraphScreen(client.id)
                                    )
                                }
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

                                Spacer(modifier = Modifier.height(30.dp))
                            }

                        }

                        BusinessContent(
                            businessEntityList = businessEntityList,
                            viewModel = viewModel,
                            clientId = client.id,
                            etcBusinessHintPopupOpen = etcBusinessPopupOpen,
                            etcBusinessHintBtnOnClick = {
                                etcBusinessPopupOpen = !etcBusinessPopupOpen
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DetailClientContent(
    modifier: Modifier = Modifier,
    graphHintPopupOpen: Boolean,
    graphHintBtnOnClick: () -> Unit,
    taskGraphBtnOnClick: () -> Unit,
    editBtnOnClick: () -> Unit,
    clientEntity: ClientEntity
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
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
            modifier = Modifier
                .size(18.dp)
                .clickable { graphHintBtnOnClick() },
            painter = painterResource(id = R.drawable.ic_info),
            tint = Color.Unspecified,
            contentDescription = null
        )

        if (graphHintPopupOpen) {
            Popup(
                onDismissRequest = { graphHintBtnOnClick() },
                offset = IntOffset(0, 70),
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp),
                    shape = Shapes.large,
                    color = Popup5E5E5E
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, top = 14.dp, bottom = 14.dp)
                            .background(Popup5E5E5E),
                        text = stringResource(id = R.string.graph_hint_popup_message),
                        style = Typography.body3,
                        color = Color.White
                    )
                }
            }
        }
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

// TODO : viewModel을 넘기는 거? 고쳐야될 거 같음
fun LazyListScope.BusinessContent(
    businessEntityList: List<BusinessEntity>,
    viewModel: ClientViewModel,
    clientId: Long,
    etcBusinessHintPopupOpen: Boolean,
    etcBusinessHintBtnOnClick: () -> Unit
) {
    val etcBusiness = businessEntityList.find { it.name == "기타" }

    item {
        FAddButton(
            onClick = {
                viewModel.navigateTo(
                    NavigateActions.DetailClientScreen
                        .toAddBusinessScreen(clientId)
                )
            },
            text = stringResource(id = R.string.add_business),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))
    }

    item {
        if (etcBusiness != null) {
            FRoundedArrowButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.navigateTo(
                        NavigateActions.DetailClientScreen
                            .toDetailEtcBusinessScreen(etcBusiness.id)
                    )
                },
                contentPadding = PaddingValues(
                    top = 24.dp,
                    bottom = 24.dp,
                    start = 20.dp,
                    end = 15.dp
                ),
                content = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(id = R.string.etc_business),
                            style = Typography.body1
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Icon(
                            modifier = Modifier
                                .size(18.dp)
                                .clickable { etcBusinessHintBtnOnClick() },
                            painter = painterResource(id = R.drawable.ic_info),
                            tint = Color.Unspecified,
                            contentDescription = null
                        )

                        if (etcBusinessHintPopupOpen) {
                            Popup(
                                onDismissRequest = { etcBusinessHintBtnOnClick() },
                                offset = IntOffset(0, 70),
                            ) {
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 20.dp, end = 20.dp),
                                    shape = Shapes.large,
                                    color = Popup5E5E5E
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                start = 20.dp,
                                                end = 20.dp,
                                                top = 14.dp,
                                                bottom = 14.dp
                                            )
                                            .background(Popup5E5E5E),
                                        text = stringResource(id = R.string.etc_business_hint_popup_message),
                                        style = Typography.body3,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(10.dp))
        }
    }

    items(businessEntityList.filter { it.name != "기타" }) { business ->
        BusinessItem(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                viewModel.navigateTo(
                    NavigateActions.DetailClientScreen
                        .toDetailBusinessScreen(business.id)
                )
            },
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

            CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
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