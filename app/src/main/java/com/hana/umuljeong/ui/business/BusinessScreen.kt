package com.hana.umuljeong.ui.business

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
import com.hana.umuljeong.R
import com.hana.umuljeong.UmuljeongScreen
import com.hana.umuljeong.data.datasource.fakeBusinessData
import com.hana.umuljeong.data.model.Business
import com.hana.umuljeong.ui.component.*
import com.hana.umuljeong.ui.theme.*
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BusinessScreen(
    modifier: Modifier = Modifier,
    uiState: BusinessListUiState,
    navController: NavController
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

    var businessKeyword by rememberSaveable { mutableStateOf("") }

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
            bottomBar = {
                UBottomBar(
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

                        USearchTextField(
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

                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }

                BusinessContent(
                    businessList = uiState.businessList,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun BusinessContent(
    modifier: Modifier = Modifier,
    businessList: List<Business>,
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

        items(businessList) { business ->
            BusinessItem(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    navController.navigate("${UmuljeongScreen.DetailBusiness.name}/${business.id}")
                },
                business = business
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
    business: Business
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
                    Text(text = business.name, style = Typography.body2)

                    Spacer(modifier = Modifier.height(10.dp))

                    Row {
                        Text(
                            text = stringResource(id = R.string.business_period),
                            style = Typography.body3,
                            color = Font70747E
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = "${business.startDate} - ${business.endDate}",
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
                        text = "${business.members.size}",
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
    UmuljeongTheme {
        BusinessItem(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

            },
            business = fakeBusinessData[0]
        )
    }
}