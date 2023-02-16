package com.hana.umuljeong.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hana.umuljeong.R
import com.hana.umuljeong.ui.component.*
import com.hana.umuljeong.ui.theme.FontDBDBDB
import com.hana.umuljeong.ui.theme.LineDBDBDB
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BusinessScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded },
        skipHalfExpanded = true,
    )

    var selectionMode by remember { mutableStateOf(DateSelectionMode.START) }

    var startDate by remember { mutableStateOf(LocalDate.now()) }
    var endDate by remember { mutableStateOf(LocalDate.now()) }

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
                    selectedDate = if (selectionMode == DateSelectionMode.START) startDate else endDate,
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
                    Column(modifier = Modifier.width(335.dp)) {
                        Spacer(modifier = Modifier.height(20.dp))

                        var businessKeyword by remember { mutableStateOf("") }
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
                                hint = "시작 일자",
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
                                hint = "종료 일자",
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
            }
        }
    }
}

@Composable
fun BusinessContent(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

    }
}