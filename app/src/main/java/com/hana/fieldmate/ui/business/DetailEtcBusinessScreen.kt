package com.hana.fieldmate.ui.business

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hana.fieldmate.R
import com.hana.fieldmate.data.ErrorType
import com.hana.fieldmate.ui.DialogType
import com.hana.fieldmate.ui.business.viewmodel.BusinessViewModel
import com.hana.fieldmate.ui.component.*
import com.hana.fieldmate.ui.navigation.NavigateActions
import com.hana.fieldmate.ui.theme.BgF1F1F5
import com.hana.fieldmate.ui.theme.Typography
import com.hana.fieldmate.ui.theme.title2

@Composable
fun DetailEtcBusinessScreen(
    modifier: Modifier = Modifier,
    viewModel: BusinessViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val business = uiState.business

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

    LaunchedEffect(true) {
        viewModel.loadBusiness()
    }

    Scaffold(
        topBar = {
            FAppBarWithBackBtn(
                title = stringResource(R.string.detail_etc),
                backBtnOnClick = {
                    viewModel.navigateTo(NavigateActions.navigateUp())
                }
            )
        },
    ) { innerPadding ->
        Box(modifier = modifier.padding(innerPadding)) {
            LoadingContent(loadingState = uiState.businessLoadingState) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp)
                ) {
                    Spacer(modifier = Modifier.height(30.dp))

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
                                text = business.name,
                                style = Typography.title2
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        FRoundedArrowButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                viewModel.navigateTo(
                                    NavigateActions.DetailEtcBusinessScreen
                                        .toBusinessTaskGraphScreen(business.id)
                                )
                            },
                            content = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        modifier = Modifier.size(40.dp),
                                        painter = painterResource(id = R.drawable.ic_graph),
                                        tint = Color.Unspecified,
                                        contentDescription = null
                                    )

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Text(
                                        text = stringResource(id = R.string.work_graph),
                                        style = Typography.body2
                                    )
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        FRoundedArrowButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                viewModel.navigateTo(
                                    NavigateActions.DetailEtcBusinessScreen
                                        .toBusinessSummaryTaskScreen(business.id)
                                )
                            },
                            content = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        modifier = Modifier.size(40.dp),
                                        painter = painterResource(id = R.drawable.ic_calendar),
                                        tint = Color.Unspecified,
                                        contentDescription = null
                                    )

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Text(
                                        text = stringResource(id = R.string.task_by_day),
                                        style = Typography.body2
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
