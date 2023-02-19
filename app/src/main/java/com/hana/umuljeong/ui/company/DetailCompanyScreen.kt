package com.hana.umuljeong.ui.company

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hana.umuljeong.R
import com.hana.umuljeong.UmuljeongScreen
import com.hana.umuljeong.data.datasource.fakeBusinessData
import com.hana.umuljeong.data.model.Business
import com.hana.umuljeong.data.model.Company
import com.hana.umuljeong.ui.business.BusinessItem
import com.hana.umuljeong.ui.component.*
import com.hana.umuljeong.ui.theme.*
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DetailCompanyScreen(
    modifier: Modifier = Modifier,
    uiState: CompanyUiState,
    navController: NavController,
    addBtnOnClick: () -> Unit
) {
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
                UAppBarWithEditBtn(
                    title = stringResource(id = R.string.detail_company),
                    backBtnOnClick = {
                        navController.navigateUp()
                    },
                    editBtnOnClick = {
                        navController.navigate("${UmuljeongScreen.EditCompany}/${uiState.company.id}")
                    }
                )
            },
        ) { innerPadding ->
            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Spacer(modifier = Modifier.height(30.dp))

                    DetailCompanyContent(company = uiState.company)

                    Spacer(modifier = Modifier.height(60.dp))

                    Row(modifier = Modifier.width(335.dp)) {
                        Text(
                            text = stringResource(id = R.string.search_business),
                            style = Typography.title2
                        )
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    Column(modifier = Modifier.width(335.dp)) {
                        var businessKeyword by rememberSaveable { mutableStateOf("") }
                        USearchTextField(
                            modifier = Modifier.fillMaxWidth(),
                            msgContent = businessKeyword,
                            hint = stringResource(id = R.string.search_business_hint),
                            onValueChange = { businessKeyword = it }
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.width(335.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            UDateField(
                                modifier = Modifier.width(158.dp),
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
                                modifier = Modifier.width(158.dp),
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

                        Spacer(modifier = Modifier.height(40.dp))
                    }
                }

                item {
                    UAddButton(
                        onClick = addBtnOnClick,
                        text = stringResource(id = R.string.add_business),
                        modifier = Modifier.width(335.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }

                BusinessContent(
                    businessList = fakeBusinessData,
                    navController = navController
                )

                item {
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DetailCompanyContent(
    modifier: Modifier = Modifier,
    company: Company
) {
    Row(
        modifier = modifier.width(335.dp),
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
            text = company.name,
            style = Typography.title2
        )
    }

    Spacer(modifier = Modifier.height(30.dp))

    PhoneItem(modifier = Modifier.width(335.dp), name = "기업 대표 전화", phone = company.phone)

    Spacer(modifier = Modifier.height(10.dp))

    PhoneItem(modifier = Modifier.width(335.dp), name = "영업 담당자 전화", phone = company.phone)

    Spacer(modifier = Modifier.height(60.dp))

    Row(modifier = Modifier.width(335.dp)) {
        Text(
            text = "${company.name}와 함께한 사업",
            style = Typography.title2
        )
    }

    Spacer(modifier = Modifier.height(30.dp))

    Row(
        modifier = Modifier.width(335.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Surface(
            onClick = { },
            modifier = Modifier.size(160.dp),
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(id = R.string.visit_number),
                            style = Typography.body1
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_info),
                            tint = Color.Unspecified,
                            contentDescription = null
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Icon(
                        painter = painterResource(id = R.drawable.ic_graph),
                        tint = Color.Unspecified,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "${company.visitNum}",
                        style = Typography.title1
                    )
                }
            }
        }

        Surface(
            modifier = Modifier.size(160.dp),
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
                        text = "${company.businessNum}",
                        style = Typography.title1
                    )
                }
            }
        }
    }
}

fun LazyListScope.BusinessContent(
    modifier: Modifier = Modifier,
    businessList: List<Business>,
    navController: NavController
) {
    items(businessList) { business ->
        BusinessItem(
            modifier = Modifier.width(335.dp),
            onClick = { /*TODO*/ },
            business = business
        )

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

            CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                IconButton(
                    onClick = { }
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

@Preview(showBackground = true)
@Composable
fun PreviewDetailCustomerScreen() {
    UmuljeongTheme {
        DetailCompanyScreen(
            uiState = CompanyUiState(),
            navController = rememberNavController(),
            addBtnOnClick = { })
    }
}