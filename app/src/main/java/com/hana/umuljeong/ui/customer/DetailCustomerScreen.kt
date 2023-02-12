package com.hana.umuljeong.ui.customer

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
import com.hana.umuljeong.data.datasource.fakeCustomerData
import com.hana.umuljeong.ui.component.UAppBarWithEditBtn
import com.hana.umuljeong.ui.theme.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DetailCustomerScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    customerId: Long
) {
    Scaffold(
        topBar = {
            UAppBarWithEditBtn(
                title = R.string.detail_customer,
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

            Row(
                modifier = Modifier.width(335.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_profile),
                    tint = Color.Unspecified,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(15.dp))

                Column {
                    Text(
                        text = fakeCustomerData[customerId.toInt()].name,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    )

                    Text(text = "담당부서")
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            Surface(
                modifier = Modifier.width(335.dp),
                shape = Shapes.large,
                color = BgF1F1F5,
                elevation = 0.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 10.dp, start = 15.dp, end = 15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Text(text = stringResource(id = R.string.customer_phone))

                        Spacer(modifier = Modifier.width(15.dp))

                        Text(text = fakeCustomerData[customerId.toInt()].phone)
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

            Spacer(modifier = Modifier.height(85.dp))

            Row(
                modifier = Modifier.width(335.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "${fakeCustomerData[customerId.toInt()].name}와 함께한 사업")
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
                    color = BgD3D3D3,
                    elevation = 0.dp,
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

                            Spacer(modifier = Modifier.height(20.dp))

                            Icon(
                                painter = painterResource(id = R.drawable.ic_graph),
                                tint = Color.Unspecified,
                                contentDescription = null
                            )

                            Spacer(modifier = Modifier.height(15.dp))

                            Row {
                                Text(text = "총 방문 건수", color = Main356DF8)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "${fakeCustomerData[customerId.toInt()].visitNum}")
                            }
                        }
                    }
                }

                Surface(
                    onClick = { },
                    modifier = Modifier.size(160.dp),
                    shape = Shapes.large,
                    color = BgD3D3D3,
                    elevation = 0.dp,
                ) {
                    Box {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(20.dp))

                            Text(text = stringResource(id = R.string.business_number))
                        }

                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "${fakeCustomerData[customerId.toInt()].businessNum}",
                                style = TextStyle(
                                    fontSize = 26.sp,
                                    color = Main356DF8
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDetailCustomerScreen() {
    UmuljeongTheme {
        DetailCustomerScreen(navController = rememberNavController(), customerId = 0L)
    }
}