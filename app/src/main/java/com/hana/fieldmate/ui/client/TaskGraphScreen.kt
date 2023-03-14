package com.hana.fieldmate.ui.client

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hana.fieldmate.R
import com.hana.fieldmate.domain.model.TaskStatisticEntity
import com.hana.fieldmate.ui.client.viewmodel.ClientUiState
import com.hana.fieldmate.ui.component.FAppBarWithBackBtn
import com.hana.fieldmate.ui.component.RoundedLinearProgressBar
import com.hana.fieldmate.ui.setting.CategoryTag
import com.hana.fieldmate.ui.theme.Typography
import com.hana.fieldmate.ui.theme.body3
import com.hana.fieldmate.ui.theme.title2

@Composable
fun TaskGraphScreen(
    modifier: Modifier = Modifier,
    uiState: ClientUiState,
    loadTaskGraph: () -> Unit,
    navController: NavController
) {
    val taskStatisticEntityList = uiState.taskStatisticList

    LaunchedEffect(true) {
        loadTaskGraph()
    }

    Scaffold(
        topBar = {
            FAppBarWithBackBtn(
                title = stringResource(id = R.string.total_work_graph),
                backBtnOnClick = { navController.navigateUp() }
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

                if (taskStatisticEntityList.isNotEmpty()) {
                    BarGraph(
                        data = taskStatisticEntityList
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
    val maxValue = data.maxOf { it.count.toFloat() }

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
                Row(modifier = Modifier.width(80.dp)) {
                    CategoryTag(text = statistic.name, color = categoryColor)
                }

                BoxWithConstraints {
                    val progress = statistic.count / maxValue

                    RoundedLinearProgressBar(
                        modifier = Modifier.fillMaxWidth(),
                        progress = progress,
                        height = 26.dp,
                        color = categoryColor.copy(alpha = 0.4f)
                    )

                    Row(
                        modifier = Modifier
                            .width(maxWidth * progress)
                            .height(26.dp)
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