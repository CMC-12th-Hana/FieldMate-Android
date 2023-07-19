package com.hana.fieldmate.ui.business

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hana.fieldmate.R
import com.hana.fieldmate.data.ErrorType
import com.hana.fieldmate.domain.toMemberEntity
import com.hana.fieldmate.ui.DialogType
import com.hana.fieldmate.ui.business.viewmodel.BusinessViewModel
import com.hana.fieldmate.ui.component.BackToLoginDialog
import com.hana.fieldmate.ui.component.ErrorDialog
import com.hana.fieldmate.ui.component.FAppBarWithEditBtn
import com.hana.fieldmate.ui.component.LoadingContent
import com.hana.fieldmate.ui.member.MemberItem
import com.hana.fieldmate.ui.navigation.NavigateActions
import com.hana.fieldmate.ui.theme.Font191919
import com.hana.fieldmate.ui.theme.Shapes
import com.hana.fieldmate.ui.theme.Typography
import com.hana.fieldmate.ui.theme.body3

@Composable
fun BusinessMemberScreen(
    modifier: Modifier = Modifier,
    viewModel: BusinessViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
            FAppBarWithEditBtn(
                title = stringResource(id = R.string.business_members),
                backBtnOnClick = {
                    viewModel.navigateTo(NavigateActions.navigateUp())
                },
                editBtnOnClick = {
                    viewModel.navigateTo(
                        NavigateActions.BusinessMemberScreen
                            .toSelectBusinessMemberScreen(uiState.business.id)
                    )
                }
            )
        }
    ) { innerPadding ->
        LoadingContent(uiState.memberNameListLoadingState) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyColumn(
                    modifier = modifier
                        .padding(start = 20.dp, end = 20.dp)
                        .weight(1f)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    items(viewModel.selectedMemberList) { member ->
                        MemberItem(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                viewModel.navigateTo(
                                    NavigateActions.BusinessMemberScreen
                                        .toDetailMemberScreen(member.id)
                                )
                            },
                            memberEntity = member.toMemberEntity()
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }

                Spacer(modifier = Modifier.fillMaxHeight())

                Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp)) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = Shapes.large,
                        color = Font191919.copy(alpha = 0.7f),
                        elevation = 0.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 15.dp,
                                    bottom = 15.dp,
                                    start = 20.dp,
                                    end = 20.dp
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(id = R.string.select_member_hint),
                                style = Typography.body3,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(50.dp))
                }
            }
        }
    }
}