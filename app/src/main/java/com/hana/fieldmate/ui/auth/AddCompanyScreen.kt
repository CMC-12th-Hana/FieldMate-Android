package com.hana.fieldmate.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hana.fieldmate.App
import com.hana.fieldmate.R
import com.hana.fieldmate.data.ErrorType
import com.hana.fieldmate.ui.DialogType
import com.hana.fieldmate.ui.auth.viewmodel.CompanyViewModel
import com.hana.fieldmate.ui.component.*
import com.hana.fieldmate.ui.navigation.NavigateActions
import com.hana.fieldmate.ui.theme.Font70747E
import com.hana.fieldmate.ui.theme.Typography
import com.hana.fieldmate.ui.theme.body4
import com.hana.fieldmate.ui.theme.title1

@Composable
fun AddCompanyScreen(
    modifier: Modifier = Modifier,
    viewModel: CompanyViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val userInfo = App.getInstance().getUserInfo()

    when (uiState.dialog) {
        is DialogType.Error -> {
            when (val error = (uiState.dialog as DialogType.Error).errorType) {
                is ErrorType.JwtExpired -> {
                    BackToLoginDialog(onClose = { viewModel.backToLogin() })
                }
                is ErrorType.General -> {
                    ErrorDialog(
                        errorMessage = error.errorMessage,
                        onClose = { viewModel.onDialogClosed() }
                    )
                }
            }
        }
        else -> {}
    }

    var companyName by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            FAppBarWithBackBtn(
                title = stringResource(id = R.string.register),
                backBtnOnClick = { viewModel.navigateTo(NavigateActions.navigateUp()) }
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
                    text = stringResource(id = R.string.add_company),
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
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(id = R.string.compnay_name_info),
                    style = Typography.body4,
                    color = Font70747E
                )

                Spacer(modifier = Modifier.height(20.dp))

                Label(text = stringResource(id = R.string.leader_name))

                Spacer(modifier = Modifier.height(8.dp))

                FTextField(
                    modifier = Modifier.fillMaxWidth(),
                    msgContent = userInfo.userName,
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
                        onClick = { viewModel.createCompany(companyName) }
                    )

                    Spacer(modifier = Modifier.height(50.dp))
                }
            }
        }
    }
}