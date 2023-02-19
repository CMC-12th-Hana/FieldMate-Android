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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hana.umuljeong.R
import com.hana.umuljeong.ui.auth.Label
import com.hana.umuljeong.ui.component.*
import com.hana.umuljeong.ui.theme.FontDBDBDB
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddBusinessScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    addMemberBtnOnClick: () -> Unit,
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

    var name by rememberSaveable { mutableStateOf("") }
    var profit by rememberSaveable { mutableStateOf("") }

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
                UAppBarWithBackBtn(
                    title = stringResource(id = R.string.add_business),
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
                        .verticalScroll(rememberScrollState())
                        .weight(1f)
                ) {
                    Spacer(modifier = Modifier.height(30.dp))

                    Label(text = stringResource(id = R.string.business_name))
                    Spacer(modifier = Modifier.height(8.dp))
                    UTextField(
                        modifier = Modifier.width(335.dp),
                        msgContent = name,
                        hint = stringResource(R.string.business_name_hint)
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    Label(text = stringResource(id = R.string.expected_business_period))
                    Spacer(modifier = Modifier.height(8.dp))
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

                    Spacer(modifier = Modifier.height(30.dp))

                    Label(text = stringResource(id = R.string.add_members))
                    Spacer(modifier = Modifier.height(8.dp))
                    URoundedArrowButton(
                        modifier = Modifier.width(335.dp),
                        onClick = addMemberBtnOnClick,
                        text = stringResource(id = R.string.get_member_profile),
                        icon = painterResource(id = R.drawable.ic_member_profile)
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    Label(text = stringResource(id = R.string.add_members))
                    Spacer(modifier = Modifier.height(8.dp))
                    UTextField(
                        modifier = Modifier.width(335.dp),
                        msgContent = profit,
                        hint = stringResource(R.string.profit_hint)
                    )
                }

                Column {
                    Spacer(Modifier.height(40.dp))

                    UButton(
                        modifier = Modifier.width(335.dp),
                        text = stringResource(id = R.string.complete),
                        onClick = addBtnOnClick
                    )

                    Spacer(Modifier.height(50.dp))
                }
            }
        }
    }
}