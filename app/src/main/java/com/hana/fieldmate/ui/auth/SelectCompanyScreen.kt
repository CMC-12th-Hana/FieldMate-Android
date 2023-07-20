package com.hana.fieldmate.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hana.fieldmate.App
import com.hana.fieldmate.R
import com.hana.fieldmate.data.ErrorType
import com.hana.fieldmate.data.local.UserInfo
import com.hana.fieldmate.ui.DialogEvent
import com.hana.fieldmate.ui.auth.viewmodel.CompanyViewModel
import com.hana.fieldmate.ui.component.*
import com.hana.fieldmate.ui.navigation.NavigateActions
import com.hana.fieldmate.ui.theme.Font191919
import com.hana.fieldmate.ui.theme.Main356DF8
import com.hana.fieldmate.ui.theme.Typography
import com.hana.fieldmate.ui.theme.body3
import kotlinx.coroutines.runBlocking

@Composable
fun SelectCompanyScreen(
    modifier: Modifier = Modifier,
    viewModel: CompanyViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState.dialog) {
        is DialogEvent.Select -> {
            SelectCompanyDialog(
                userInfo = App.getInstance().getUserInfo(),
                onSelect = { viewModel.joinCompany() },
                onClose = { viewModel.onDialogClosed() }
            )
        }
        is DialogEvent.Error -> {
            when (val error = (uiState.dialog as DialogEvent.Error).errorType) {
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

    LaunchedEffect(true) {
        runBlocking { viewModel.fetchUserInfo() }
    }

    LoadingContent(loadingState = uiState.companyLoadingState) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FImageButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.45f, true),
                    imageModifier = modifier.size(width = 120.dp, height = 120.dp),
                    onClick = { viewModel.navigateTo(NavigateActions.SelectCompanyScreen.toAddCompanyScreen()) },
                    title = stringResource(id = R.string.add_company),
                    description = stringResource(id = R.string.add_company_info_one),
                    image = R.drawable.img_add_company
                )

                Spacer(modifier = Modifier.height(10.dp))

                FImageButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.45f, true),
                    imageModifier = modifier.size(width = 120.dp, height = 120.dp),
                    onClick = { viewModel.openSelectCompanyDialog() },
                    title = stringResource(id = R.string.join_company),
                    description = stringResource(R.string.join_company_info),
                    image = R.drawable.img_join_company
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SelectCompanyDialog(
    modifier: Modifier = Modifier,
    userInfo: UserInfo,
    onSelect: () -> Unit,
    onClose: () -> Unit
) {
    FDialog(
        content = {
            // 등록된 회사가 존재하지 않을 때
            if (userInfo.companyId == -1L) {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.join_company_info_if_not_exists),
                        style = Typography.body1,
                        color = Main356DF8
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        text = stringResource(R.string.join_company_info_two_if_not_exists),
                        textAlign = TextAlign.Center,
                        style = Typography.body2
                    )
                }
            } else {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.join_company_info_if_exists),
                        style = Typography.body1,
                        color = Main356DF8
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    FRoundedArrowButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onSelect,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Main356DF8.copy(alpha = 0.2f),
                            contentColor = Font191919
                        ),
                        content = {
                            Text(
                                text = userInfo.companyName,
                                style = Typography.body3
                            )
                        }
                    )
                }
            }
        },
        button = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                onClick = onClose
            ) {
                Text(
                    modifier = Modifier.padding(top = 15.dp, bottom = 15.dp),
                    text = stringResource(id = R.string.confirm),
                    style = Typography.body1,
                    textAlign = TextAlign.Center,
                    color = Main356DF8
                )
            }
        }
    )
}
