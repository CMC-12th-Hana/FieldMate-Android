package com.hana.fieldmate.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hana.fieldmate.R
import com.hana.fieldmate.ui.Event
import com.hana.fieldmate.ui.UserInfo
import com.hana.fieldmate.ui.component.FAppBarWithBackBtn
import com.hana.fieldmate.ui.component.FButton
import com.hana.fieldmate.ui.component.FTextField
import com.hana.fieldmate.ui.theme.Typography
import com.hana.fieldmate.ui.theme.title1
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun JoinCompanyScreen(
    modifier: Modifier = Modifier,
    eventsFlow: Flow<Event>,
    sendEvent: (Event) -> Unit,
    userInfo: UserInfo,
    navController: NavController,
    confirmBtnOnClick: (String) -> Unit
) {
    var companyName by rememberSaveable { mutableStateOf("") }
    var leaderName by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(userInfo) {
        leaderName = userInfo.userName

        eventsFlow.collectLatest { event ->
            when (event) {
                is Event.NavigateTo -> navController.navigate(event.destination)
                is Event.NavigatePopUpTo -> navController.navigate(event.destination) {
                    popUpTo(event.popUpDestination) {
                        inclusive = event.inclusive
                    }
                }
                is Event.NavigateUp -> navController.navigateUp()
                is Event.Dialog -> {}
            }
        }
    }

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
                    readOnly = true
                )

                Spacer(modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f))

                Column {
                    FButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.complete),
                        onClick = { confirmBtnOnClick(companyName) }
                    )

                    Spacer(modifier = Modifier.height(50.dp))
                }
            }
        }
    }
}