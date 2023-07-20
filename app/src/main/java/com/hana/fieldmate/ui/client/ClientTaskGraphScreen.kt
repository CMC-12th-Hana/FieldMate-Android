package com.hana.fieldmate.ui.client

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hana.fieldmate.R
import com.hana.fieldmate.data.ErrorType
import com.hana.fieldmate.ui.DialogEvent
import com.hana.fieldmate.ui.business.BarGraph
import com.hana.fieldmate.ui.client.viewmodel.ClientViewModel
import com.hana.fieldmate.ui.component.BackToLoginDialog
import com.hana.fieldmate.ui.component.ErrorDialog
import com.hana.fieldmate.ui.component.FAppBarWithBackBtn
import com.hana.fieldmate.ui.navigation.NavigateActions
import com.hana.fieldmate.ui.theme.Typography
import com.hana.fieldmate.ui.theme.title2

@Composable
fun ClientTaskGraphScreen(
    modifier: Modifier = Modifier,
    viewModel: ClientViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val taskStatisticList = uiState.taskStatisticList

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

    LaunchedEffect(true) {
        viewModel.loadTaskGraph()
    }

    Scaffold(
        topBar = {
            FAppBarWithBackBtn(
                title = stringResource(id = R.string.work_graph),
                backBtnOnClick = {
                    viewModel.navigateTo(NavigateActions.navigateUp())
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                Text(text = stringResource(id = R.string.work_category), style = Typography.title2)

                Spacer(modifier = Modifier.height(30.dp))

                if (taskStatisticList.isNotEmpty()) {
                    BarGraph(
                        data = taskStatisticList
                    )
                }
            }
        }
    }
}
