package com.hana.umuljeong.ui.customer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hana.umuljeong.R
import com.hana.umuljeong.UmuljeongScreen
import com.hana.umuljeong.data.datasource.fakeCustomerData
import com.hana.umuljeong.data.model.Customer
import com.hana.umuljeong.ui.component.UAddButton
import com.hana.umuljeong.ui.component.UBottomBar
import com.hana.umuljeong.ui.component.USearchTextField
import com.hana.umuljeong.ui.theme.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomerScreen(
    modifier: Modifier = Modifier,
    addBtnOnClick: () -> Unit,
    navController: NavController
) {
    Scaffold(
        bottomBar = {
            UBottomBar(
                navController = navController
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                var customerName by remember { mutableStateOf("") }
                USearchTextField(
                    modifier = Modifier.width(296.dp),
                    msgContent = customerName,
                    hint = stringResource(id = R.string.search_customer_hint),
                    onValueChange = { customerName = it }
                )

                CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                    IconButton(
                        onClick = { }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_sort),
                            tint = Color.Unspecified,
                            contentDescription = null
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(15.dp))

                    UAddButton(
                        onClick = addBtnOnClick,
                        text = stringResource(id = R.string.add_customer),
                        modifier = Modifier.width(335.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }

                items(fakeCustomerData) { customer ->
                    CustomerItem(
                        modifier = Modifier.width(335.dp),
                        onClick = {
                            navController.navigate("${UmuljeongScreen.DetailCustomer.name}/${customer.id}")
                        },
                        customer = customer
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(15.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomerItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    shape: Shape = Shapes.large,
    customer: Customer
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        color = BgF8F8FA,
        border = BorderStroke(width = 1.dp, color = BgD3D3D3),
        elevation = 0.dp
    ) {
        Column(
            modifier = Modifier.padding(all = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            modifier = Modifier
                                .background(
                                    color = Color(0xFF102043), shape = shapes.small
                                )
                                .padding(top = 3.dp, bottom = 3.dp, start = 8.dp, end = 8.dp),
                            text = stringResource(id = R.string.visit_number) + " ${customer.visitNum}",
                            fontSize = 12.sp,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            modifier = Modifier
                                .background(
                                    color = Color.Transparent, shape = shapes.small
                                )
                                .border(width = 1.dp, color = Color(0xFFBECCE9))
                                .padding(top = 3.dp, bottom = 3.dp, start = 8.dp, end = 8.dp),
                            text = stringResource(id = R.string.business_number) + " ${customer.businessNum}",
                            fontSize = 12.sp,
                            color = Main356DF8
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = customer.name,
                        fontWeight = FontWeight.Bold
                    )
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
}

@Preview
@Composable
fun PreviewCustomer() {
    UmuljeongTheme {
        CustomerItem(onClick = { }, customer = fakeCustomerData[0])
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCustomerScreen() {
    UmuljeongTheme {
        CustomerScreen(addBtnOnClick = { }, navController = rememberNavController())
    }
}