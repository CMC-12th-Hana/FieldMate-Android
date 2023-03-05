package com.hana.fieldmate.ui.business

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hana.fieldmate.R
import com.hana.fieldmate.data.local.fakeCategorySelectionData
import com.hana.fieldmate.data.local.fakeVisitData
import com.hana.fieldmate.ui.component.FAppBarWithExitBtn
import com.hana.fieldmate.ui.component.RoundedLinearProgressBar
import com.hana.fieldmate.ui.setting.CategoryTag
import com.hana.fieldmate.ui.theme.CategoryColor
import com.hana.fieldmate.ui.theme.Typography
import com.hana.fieldmate.ui.theme.body3
import com.hana.fieldmate.ui.theme.title2

@Composable
fun GraphScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Scaffold(
        topBar = {
            FAppBarWithExitBtn(
                title = stringResource(id = R.string.work_graph),
                exitBtnOnClick = { navController.navigateUp() }
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

                BarGraph(
                    data = fakeVisitData
                )
            }
        }
    }
}

@Composable
fun BarGraph(
    modifier: Modifier = Modifier,
    data: List<Pair<String, Int>>
) {
    val maxValue = data.maxOf { it.second.toFloat() }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(26.dp)
    ) {
        data.forEach { pair ->
            val categoryColor =
                CategoryColor[fakeCategorySelectionData.indexOf(pair.first)]

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Row(modifier = Modifier.width(80.dp)) {
                    CategoryTag(text = pair.first, color = categoryColor)
                }

                BoxWithConstraints {
                    val progress = pair.second / maxValue

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
                        Text(text = "${pair.second}", style = Typography.body3, color = Color.White)
                    }
                }
            }
        }
    }
}