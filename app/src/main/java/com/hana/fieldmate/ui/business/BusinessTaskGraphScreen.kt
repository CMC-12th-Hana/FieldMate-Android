package com.hana.fieldmate.ui.business

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hana.fieldmate.R
import com.hana.fieldmate.data.ErrorType
import com.hana.fieldmate.domain.model.TaskStatisticEntity
import com.hana.fieldmate.ui.DialogType
import com.hana.fieldmate.ui.business.viewmodel.BusinessViewModel
import com.hana.fieldmate.ui.component.BackToLoginDialog
import com.hana.fieldmate.ui.component.ErrorDialog
import com.hana.fieldmate.ui.component.FAppBarWithBackBtn
import com.hana.fieldmate.ui.component.RoundedLinearProgressBar
import com.hana.fieldmate.ui.navigation.NavigateActions
import com.hana.fieldmate.ui.setting.CategoryTag
import com.hana.fieldmate.ui.theme.Typography
import com.hana.fieldmate.ui.theme.body3
import com.hana.fieldmate.ui.theme.title2

@Composable
fun BusinessTaskGraphScreen(
    modifier: Modifier = Modifier,
    viewModel: BusinessViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val taskStatisticList = uiState.taskStatisticList

    when (uiState.dialog) {
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

@Composable
fun BarGraph(
    modifier: Modifier = Modifier,
    data: List<TaskStatisticEntity>
) {
    val maxValue =
        if (data.maxOf { it.count.toFloat() } == 0f) 1f else data.maxOf { it.count.toFloat() }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(26.dp)
    ) {
        data.forEach { statistic ->
            val categoryColor = statistic.color

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Row(modifier = Modifier.width(81.dp)) {
                    CategoryTag(text = statistic.name, color = categoryColor)
                }

                BoxWithConstraints {
                    val progress = 0.1f + (statistic.count / maxValue) * 0.9f

                    RoundedLinearProgressBar(
                        modifier = Modifier.fillMaxWidth(),
                        progress = progress,
                        height = 22.dp,
                        color = categoryColor.copy(alpha = 0.4f)
                    )

                    Row(
                        modifier = Modifier
                            .width(maxWidth * progress)
                            .height(22.dp)
                            .padding(end = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "${statistic.count}",
                            style = Typography.body3,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}