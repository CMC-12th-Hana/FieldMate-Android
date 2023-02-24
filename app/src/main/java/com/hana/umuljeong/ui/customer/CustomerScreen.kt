package com.hana.umuljeong.ui.customer

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hana.umuljeong.R
import com.hana.umuljeong.UmuljeongScreen
import com.hana.umuljeong.data.datasource.fakeCompanyData
import com.hana.umuljeong.data.model.Company
import com.hana.umuljeong.toFormattedPhoneNum
import com.hana.umuljeong.ui.component.UAddButton
import com.hana.umuljeong.ui.component.UBottomBar
import com.hana.umuljeong.ui.component.USearchTextField
import com.hana.umuljeong.ui.theme.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomerScreen(
    modifier: Modifier = Modifier,
    uiState: CustomerListUiState,
    addBtnOnClick: () -> Unit,
    navController: NavController
) {
    var customerName by rememberSaveable { mutableStateOf("") }

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
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, LineDBDBDB),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp)
                ) {
                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        USearchTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
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

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            CustomerContent(
                customerList = uiState.companyList,
                navController = navController,
                addBtnOnClick = addBtnOnClick
            )
        }
    }
}

@Composable
fun CustomerContent(
    modifier: Modifier = Modifier,
    customerList: List<Company>,
    navController: NavController,
    addBtnOnClick: () -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(20.dp))

            UAddButton(
                onClick = addBtnOnClick,
                text = stringResource(id = R.string.add_customer),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))
        }

        items(customerList) { company ->
            CustomerItem(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    navController.navigate("${UmuljeongScreen.DetailCustomer.name}/${company.id}")
                },
                company = company
            )
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomerItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    shape: Shape = Shapes.large,
    company: Company
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        color = BgF8F8FA,
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
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row {
                            Text(
                                modifier = Modifier
                                    .background(color = Main356DF8, shape = shapes.small)
                                    .padding(top = 3.dp, bottom = 3.dp, start = 8.dp, end = 8.dp),
                                text = stringResource(id = R.string.visit_number) + " ${company.visitNum}",
                                style = Typography.body6,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                modifier = Modifier
                                    .background(color = Color.Transparent, shape = shapes.small)
                                    .border(width = 1.dp, color = Color(0xFFBECCE9))
                                    .padding(top = 3.dp, bottom = 3.dp, start = 8.dp, end = 8.dp),
                                text = stringResource(id = R.string.business_number) + " ${company.businessNum}",
                                style = Typography.body6,
                                color = Main356DF8
                            )
                        }

                        val context = LocalContext.current

                        CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                            IconButton(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_DIAL).apply {
                                        data = Uri.parse(company.phone.toFormattedPhoneNum())
                                    }
                                    context.startActivity(intent)
                                }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_call),
                                    tint = Color.Unspecified,
                                    contentDescription = null
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = company.name,
                        style = Typography.body1
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewCompany() {
    UmuljeongTheme {
        CustomerItem(onClick = { }, company = fakeCompanyData[0])
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCompanyScreen() {
    UmuljeongTheme {
        CustomerScreen(
            uiState = CustomerListUiState(),
            addBtnOnClick = { },
            navController = rememberNavController()
        )
    }
}