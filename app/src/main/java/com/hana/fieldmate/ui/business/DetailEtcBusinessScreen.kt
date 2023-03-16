package com.hana.fieldmate.ui.business

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hana.fieldmate.FieldMateScreen
import com.hana.fieldmate.R
import com.hana.fieldmate.ui.DialogAction
import com.hana.fieldmate.ui.DialogState
import com.hana.fieldmate.ui.Event
import com.hana.fieldmate.ui.business.viewmodel.BusinessUiState
import com.hana.fieldmate.ui.component.ErrorDialog
import com.hana.fieldmate.ui.component.FAppBarWithBackBtn
import com.hana.fieldmate.ui.component.FRoundedArrowButton
import com.hana.fieldmate.ui.component.LoadingContent
import com.hana.fieldmate.ui.theme.BgF1F1F5
import com.hana.fieldmate.ui.theme.Typography
import com.hana.fieldmate.ui.theme.title2
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DetailEtcBusinessScreen(
    modifier: Modifier = Modifier,
    uiState: BusinessUiState,
    eventsFlow: Flow<Event>,
    sendEvent: (Event) -> Unit,
    loadBusiness: () -> Unit,
    navController: NavController
) {
    val businessEntity = uiState.business

    var errorDialogOpen by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf("") }

    if (errorDialogOpen) ErrorDialog(
        errorMessage = errorMessage,
        onClose = { sendEvent(Event.Dialog(DialogState.Error, DialogAction.Close)) }
    )

    LaunchedEffect(true) {
        loadBusiness()

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

    Scaffold(
        topBar = {
            FAppBarWithBackBtn(
                title = stringResource(R.string.detail_etc),
                backBtnOnClick = { navController.navigateUp() }
            )
        },
    ) { innerPadding ->
        Box(modifier = modifier.padding(innerPadding)) {
            LoadingContent(loadingState = uiState.businessLoadingState) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp)
                ) {
                    Spacer(modifier = Modifier.height(30.dp))

                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
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

                            Text(
                                text = businessEntity.name,
                                style = Typography.title2
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        FRoundedArrowButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { navController.navigate(FieldMateScreen.TaskGraph.name) },
                            content = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        modifier = Modifier.size(40.dp),
                                        painter = painterResource(id = R.drawable.ic_graph),
                                        tint = Color.Unspecified,
                                        contentDescription = null
                                    )

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Text(
                                        text = stringResource(id = R.string.work_graph),
                                        style = Typography.body2
                                    )
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        FRoundedArrowButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { navController.navigate("${FieldMateScreen.SummaryTask.name}/${businessEntity.id}") },
                            content = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        modifier = Modifier.size(40.dp),
                                        painter = painterResource(id = R.drawable.ic_calendar),
                                        tint = Color.Unspecified,
                                        contentDescription = null
                                    )

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Text(
                                        text = stringResource(id = R.string.task_by_day),
                                        style = Typography.body2
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
