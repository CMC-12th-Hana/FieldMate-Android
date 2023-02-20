package com.hana.umuljeong.ui.business

import androidx.compose.foundation.layout.*
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hana.umuljeong.R
import com.hana.umuljeong.data.datasource.fakeCategorySelectionData
import com.hana.umuljeong.data.datasource.fakeVisitData
import com.hana.umuljeong.ui.component.UAppBarWithExitBtn
import com.hana.umuljeong.ui.setting.CategoryTag
import com.hana.umuljeong.ui.theme.CategoryColor

@Composable
fun GraphScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Scaffold(
        topBar = {
            UAppBarWithExitBtn(
                title = stringResource(id = R.string.visit_num_graph),
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
            BarGraph(
                data = fakeVisitData
            )
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
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(26.dp)
    ) {
        data.forEach { pair ->
            val categoryColor =
                CategoryColor[fakeCategorySelectionData.indexOf(pair.first)]

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Row(modifier = Modifier.width(80.dp)) {
                    CategoryTag(text = pair.first, color = categoryColor)
                }

                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(26.dp),
                    progress = pair.second / maxValue,
                    backgroundColor = Color.Transparent,
                    color = categoryColor.copy(alpha = 0.4f)
                )
            }
        }
    }
}