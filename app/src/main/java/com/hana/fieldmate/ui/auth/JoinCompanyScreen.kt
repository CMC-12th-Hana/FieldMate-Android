package com.hana.fieldmate.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hana.fieldmate.R
import com.hana.fieldmate.ui.component.FAppBarWithBackBtn
import com.hana.fieldmate.ui.component.FButton
import com.hana.fieldmate.ui.component.FTextField
import com.hana.fieldmate.ui.theme.FieldMateTheme
import com.hana.fieldmate.ui.theme.Typography
import com.hana.fieldmate.ui.theme.title1

@Composable
fun JoinCompanyScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    confirmBtnOnClick: () -> Unit
) {
    var companyName by rememberSaveable { mutableStateOf("") }
    var leaderName by rememberSaveable { mutableStateOf("사용자명") }

    Scaffold(
        topBar = {
            FAppBarWithBackBtn(
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
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 20.dp, end = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = stringResource(id = R.string.add_company_info_two),
                    style = Typography.title1
                )

                Spacer(modifier = Modifier.height(30.dp))

                Label(text = stringResource(id = R.string.company_name))

                Spacer(modifier = Modifier.height(8.dp))

                FTextField(
                    modifier = Modifier.fillMaxWidth(),
                    msgContent = companyName,
                    hint = stringResource(id = R.string.company_name_hint),
                    onValueChange = { companyName = it })

                Spacer(modifier = Modifier.height(20.dp))

                Label(text = stringResource(id = R.string.leader_name))

                Spacer(modifier = Modifier.height(8.dp))

                FTextField(
                    modifier = Modifier.fillMaxWidth(),
                    msgContent = leaderName,
                    enabled = false,
                    readOnly = true,
                    onValueChange = { leaderName = it })

                Spacer(modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f))

                Column {
                    FButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.complete),
                        onClick = confirmBtnOnClick
                    )

                    Spacer(Modifier.height(50.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAddMyCompanyScreen() {
    FieldMateTheme {
        JoinCompanyScreen(
            navController = rememberNavController(),
            confirmBtnOnClick = { }
        )
    }
}