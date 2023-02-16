package com.hana.umuljeong.ui.company

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hana.umuljeong.R
import com.hana.umuljeong.data.datasource.fakeCompanyData
import com.hana.umuljeong.data.model.Company
import com.hana.umuljeong.ui.component.UAppBarWithEditBtn
import com.hana.umuljeong.ui.theme.*

@Composable
fun DetailCompanyScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    customerId: Long
) {
    Scaffold(
        topBar = {
            UAppBarWithEditBtn(
                title = stringResource(id = R.string.detail_company),
                editId = customerId,
                backBtnOnClick = {
                    navController.navigateUp()
                },
                editBtnOnClick = {

                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            DetailCompanyContent(company = fakeCompanyData[customerId.toInt()])
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DetailCompanyContent(
    modifier: Modifier = Modifier,
    company: Company
) {
    Row(
        modifier = modifier.width(335.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_company),
            tint = Color.Unspecified,
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(15.dp))

        Column {
            Text(
                text = company.name,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(text = "담당부서 / 담당자명")
        }
    }

    Spacer(modifier = Modifier.height(30.dp))

    PhoneItem(modifier = Modifier.width(335.dp), name = "기술자 담당 전화", phone = company.phone)

    Spacer(modifier = Modifier.height(10.dp))

    PhoneItem(modifier = Modifier.width(335.dp), name = "기업 대표 전화", phone = company.phone)

    Spacer(modifier = Modifier.height(10.dp))

    PhoneItem(modifier = Modifier.width(335.dp), name = "영업 담당자 전화", phone = company.phone)

    Spacer(modifier = Modifier.height(60.dp))

    Row(
        modifier = Modifier.width(335.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "${company.name}와 함께한 사업")
    }

    Spacer(modifier = Modifier.height(30.dp))

    Row(
        modifier = Modifier.width(335.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Surface(
            onClick = { },
            modifier = Modifier.size(160.dp),
            shape = Shapes.large,
            color = Color.White,
            elevation = 5.dp,
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = stringResource(id = R.string.visit_number))
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_info),
                            tint = Color.Unspecified,
                            contentDescription = null
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Icon(
                        painter = painterResource(id = R.drawable.ic_graph),
                        tint = Color.Unspecified,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "${company.visitNum}",
                        fontSize = 20.sp
                    )
                }
            }
        }

        Surface(
            onClick = { },
            modifier = Modifier.size(160.dp),
            shape = Shapes.large,
            color = Color.White,
            elevation = 5.dp,
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = stringResource(id = R.string.business_number))

                    Spacer(modifier = Modifier.height(10.dp))

                    Icon(
                        painter = painterResource(id = R.drawable.ic_business),
                        tint = Color.Unspecified,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "${company.businessNum}",
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PhoneItem(
    modifier: Modifier = Modifier,
    name: String,
    phone: String
) {
    Surface(
        modifier = modifier,
        shape = Shapes.large,
        color = BgF1F1F5,
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp, bottom = 15.dp, start = 20.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Text(text = name)

                Spacer(modifier = Modifier.width(15.dp))

                Text(text = phone)
            }

            CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                IconButton(
                    onClick = { }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_call),
                        tint = Color.Unspecified,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDetailCustomerScreen() {
    UmuljeongTheme {
        DetailCompanyScreen(navController = rememberNavController(), customerId = 0L)
    }
}