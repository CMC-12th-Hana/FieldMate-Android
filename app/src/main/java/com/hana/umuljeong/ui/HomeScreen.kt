package com.hana.umuljeong.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hana.umuljeong.R
import com.hana.umuljeong.UmuljeongScreen
import com.hana.umuljeong.data.datasource.fakeReportData
import com.hana.umuljeong.data.model.Report
import com.hana.umuljeong.ui.component.UAddButton
import com.hana.umuljeong.ui.theme.*

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    addBtnOnClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(15.dp))

        UAddButton(
            onClick = addBtnOnClick,
            text = stringResource(id = R.string.add_report),
            modifier = Modifier.width(335.dp)
        )

        Spacer(modifier = Modifier.height(15.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            items(fakeReportData) { report ->
                ReportItem(
                    onClick = {
                        navController.navigate("${UmuljeongScreen.DetailReport.name}/${report.id}")
                    },
                    modifier = Modifier.width(335.dp),
                    report = report
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ReportItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = Shapes.medium,
    report: Report
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        color = BgLightGray,
        elevation = 0.dp
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = report.name)

                Surface(
                    shape = Shapes.medium,
                    color = ButtonSkyBlue,
                    contentColor = Color.White,
                    elevation = 0.dp
                ) {
                    Text(
                        modifier = Modifier.padding(
                            top = 6.dp, bottom = 6.dp, start = 10.dp, end = 10.dp
                        ),
                        text = report.category
                    )
                }
            }


            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    UmuljeongTheme {
        HomeScreen(navController = rememberNavController(), addBtnOnClick = { })
    }
}