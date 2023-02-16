package com.hana.umuljeong.ui.company

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hana.umuljeong.R
import com.hana.umuljeong.ui.component.UAppBarWithBackBtn
import com.hana.umuljeong.ui.component.UButton
import com.hana.umuljeong.ui.component.UTextField
import com.hana.umuljeong.ui.theme.BgF8F8FA
import com.hana.umuljeong.ui.theme.UmuljeongTheme

@Composable
fun AddCompanyScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    confirmBtnOnClick: () -> Unit
) {
    Scaffold(
        topBar = {
            UAppBarWithBackBtn(
                title = stringResource(id = R.string.register),
                backBtnOnClick = {
                    navController.navigateUp()
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BgF8F8FA),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(30.dp))

                    Column(
                        modifier = Modifier.width(335.dp)
                    ) {
                        Row {
                            Text(
                                text = stringResource(id = R.string.company_name),
                                style = TextStyle(
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.sp
                                )
                            )
                            Text(
                                text = stringResource(id = R.string.star),
                                style = TextStyle(
                                    fontWeight = FontWeight.Normal,
                                    color = Color.Red,
                                    fontSize = 14.sp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        var companyName by remember { mutableStateOf("") }
                        UTextField(
                            modifier = Modifier.width(335.dp),
                            msgContent = companyName,
                            hint = stringResource(id = R.string.company_name_hint),
                            onValueChange = { companyName = it })

                        Spacer(modifier = Modifier.height(20.dp))

                        Row {
                            Text(
                                text = stringResource(id = R.string.company_phone),
                                style = TextStyle(
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.sp
                                )
                            )
                            Text(
                                text = stringResource(id = R.string.star),
                                style = TextStyle(
                                    fontWeight = FontWeight.Normal,
                                    color = Color.Red,
                                    fontSize = 14.sp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        var companyPhone by remember { mutableStateOf("") }
                        UTextField(
                            modifier = Modifier.width(335.dp),
                            msgContent = companyPhone,
                            hint = stringResource(id = R.string.company_phone_hint),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            onValueChange = { companyPhone = it })
                    }

                    Spacer(modifier = Modifier.height(30.dp))
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier.width(335.dp)
                    ) {
                        Spacer(modifier = Modifier.height(30.dp))

                        Text(text = stringResource(id = R.string.sales_manager))

                        Spacer(modifier = Modifier.height(30.dp))

                        Row {
                            Text(
                                text = stringResource(id = R.string.department_name),
                                style = TextStyle(
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.sp
                                )
                            )
                            Text(
                                text = stringResource(id = R.string.star),
                                style = TextStyle(
                                    fontWeight = FontWeight.Normal,
                                    color = Color.Red,
                                    fontSize = 14.sp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        var departmentName by remember { mutableStateOf("") }
                        UTextField(
                            modifier = Modifier.fillMaxWidth(),
                            msgContent = departmentName,
                            hint = stringResource(id = R.string.department_name_hint),
                            onValueChange = { departmentName = it })

                        Spacer(modifier = Modifier.height(20.dp))

                        Row {
                            Text(
                                text = stringResource(id = R.string.manager_name_and_phone),
                                style = TextStyle(
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.sp
                                )
                            )
                            Text(
                                text = stringResource(id = R.string.star),
                                style = TextStyle(
                                    fontWeight = FontWeight.Normal,
                                    color = Color.Red,
                                    fontSize = 14.sp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        var managerName by remember { mutableStateOf("") }
                        UTextField(
                            modifier = Modifier.fillMaxWidth(),
                            msgContent = managerName,
                            hint = stringResource(id = R.string.manager_phone_hint),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            onValueChange = { managerName = it })

                        Spacer(modifier = Modifier.height(8.dp))

                        var managerPhone by remember { mutableStateOf("") }
                        UTextField(
                            modifier = Modifier.fillMaxWidth(),
                            msgContent = managerPhone,
                            hint = stringResource(id = R.string.manager_phone_hint),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            onValueChange = { managerPhone = it })
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                UButton(
                    modifier = Modifier.width(335.dp),
                    onClick = confirmBtnOnClick
                ) {
                    Text(
                        text = stringResource(id = R.string.confirm)
                    )
                }

                Spacer(Modifier.height(50.dp))
            }
        }
    }
}

@Preview
@Composable
fun PreviewAddCompanyScreen() {
    UmuljeongTheme {
        AddCompanyScreen(navController = rememberNavController()) {

        }
    }
}