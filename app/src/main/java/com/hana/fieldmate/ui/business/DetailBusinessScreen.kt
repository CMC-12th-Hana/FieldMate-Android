package com.hana.fieldmate.ui.business

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hana.fieldmate.FieldMateScreen
import com.hana.fieldmate.R
import com.hana.fieldmate.domain.model.BusinessEntity
import com.hana.fieldmate.ui.component.FAppBarWithEditBtn
import com.hana.fieldmate.ui.component.FRoundedArrowButton
import com.hana.fieldmate.ui.component.FTextField
import com.hana.fieldmate.ui.theme.*

@Composable
fun DetailBusinessScreen(
    modifier: Modifier = Modifier,
    uiState: BusinessUiState,
    navController: NavController,
) {
    Scaffold(
        topBar = {
            FAppBarWithEditBtn(
                title = stringResource(id = R.string.detail_business),
                backBtnOnClick = {
                    navController.navigateUp()
                },
                editBtnOnClick = {
                    navController.navigate("${FieldMateScreen.EditBusiness.name}/${uiState.businessEntity.id}")
                }
            )
        },
    ) { innerPadding ->
        Box(modifier = modifier.padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(30.dp))

                DetailBusinessContent(
                    businessEntity = uiState.businessEntity,
                    navController = navController
                )
            }
        }
    }

}

@Composable
fun DetailBusinessContent(
    modifier: Modifier = Modifier,
    businessEntity: BusinessEntity,
    navController: NavController
) {
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

        Column(
            modifier = Modifier.padding(start = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    modifier = Modifier
                        .background(color = Main356DF8, shape = MaterialTheme.shapes.small)
                        .padding(top = 3.dp, bottom = 3.dp, start = 8.dp, end = 8.dp),
                    text = stringResource(id = R.string.business_period),
                    style = Typography.body6,
                    color = Color.White
                )

                Text(
                    text = "${businessEntity.startDate}~${businessEntity.endDate}",
                    style = Typography.body2
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    modifier = Modifier
                        .background(color = Color.Transparent, shape = MaterialTheme.shapes.small)
                        .border(width = 1.dp, color = Color(0xFFBECCE9))
                        .padding(top = 3.dp, bottom = 3.dp, start = 8.dp, end = 8.dp),
                    text = stringResource(id = R.string.profit),
                    style = Typography.body6,
                    color = Main356DF8
                )

                Text(
                    text = businessEntity.profit,
                    style = Typography.body2
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        FRoundedArrowButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { navController.navigate(FieldMateScreen.BusinessMember.name) },
            text = stringResource(id = R.string.get_member_profile),
            number = businessEntity.memberEntities.size,
            icon = painterResource(id = R.drawable.ic_member_profile)
        )

        Spacer(modifier = Modifier.height(10.dp))

        FRoundedArrowButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { navController.navigate(FieldMateScreen.VisitGraph.name) },
            text = stringResource(id = R.string.work_graph),
            icon = painterResource(id = R.drawable.ic_graph)
        )

        Spacer(modifier = Modifier.height(10.dp))

        FRoundedArrowButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { navController.navigate(FieldMateScreen.SummaryReport.name) },
            text = stringResource(id = R.string.report_by_day),
            icon = painterResource(id = R.drawable.ic_calendar)
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.remark),
            style = Typography.body3
        )

        Spacer(modifier = Modifier.height(10.dp))

        FTextField(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 130.dp, max = Dp.Infinity),
            textStyle = TextStyle(
                fontFamily = Pretendard,
                color = Font70747E,
                fontSize = 16.sp
            ),
            msgContent = "",
            singleLine = false,
            readOnly = true
        )

        Spacer(modifier = Modifier.height(10.dp))
    }
}