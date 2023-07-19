package com.hana.fieldmate.ui.business

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hana.fieldmate.App
import com.hana.fieldmate.R
import com.hana.fieldmate.data.ErrorType
import com.hana.fieldmate.domain.model.MemberNameEntity
import com.hana.fieldmate.ui.DialogType
import com.hana.fieldmate.ui.business.viewmodel.BusinessViewModel
import com.hana.fieldmate.ui.component.*
import com.hana.fieldmate.ui.navigation.NavigateActions
import com.hana.fieldmate.ui.theme.BgF8F8FA
import com.hana.fieldmate.ui.theme.Shapes
import com.hana.fieldmate.ui.theme.Typography

@Composable
fun SelectBusinessMemberScreen(
    modifier: Modifier = Modifier,
    viewModel: BusinessViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val userInfo = App.getInstance().getUserInfo()
    val memberList: List<MemberNameEntity> = uiState.memberNameList

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

    var memberName by remember { mutableStateOf("") }
    var selectedName by remember { mutableStateOf("") }

    LaunchedEffect(selectedName) {
        viewModel.loadCompanyMembers(userInfo.companyId, selectedName)
    }

    LaunchedEffect(true) {
        viewModel.loadBusiness()
        viewModel.loadCompanyMembers(userInfo.companyId, null)
    }

    Scaffold(
        topBar = {
            FAppBarWithBackBtn(
                title = stringResource(id = R.string.add_members),
                backBtnOnClick = {
                    viewModel.navigateTo(NavigateActions.navigateUp())
                }
            )
        }
    ) { innerPadding ->
        LoadingContent(loadingState = uiState.memberNameListLoadingState) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyColumn(
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp)
                        .weight(1f)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(20.dp))

                        FSearchTextField(
                            modifier = Modifier.fillMaxWidth(),
                            msgContent = memberName,
                            hint = stringResource(id = R.string.search_member_hint),
                            onSearch = { selectedName = it },
                            onValueChange = { memberName = it }
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    items(memberList) { member ->
                        SelectableMemberItem(
                            modifier = Modifier.fillMaxWidth(),
                            memberEntity = member,
                            selected = viewModel.selectedMemberList.contains(member),
                            selectMember = viewModel::selectMember,
                            unselectMember = viewModel::removeMember
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }

                Spacer(modifier = Modifier.fillMaxHeight())

                Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp)) {
                    Spacer(Modifier.height(40.dp))

                    FButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.complete),
                        onClick = { viewModel.updateBusinessMembers() }
                    )

                    Spacer(Modifier.height(50.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SelectableMemberItem(
    modifier: Modifier = Modifier,
    shape: Shape = Shapes.large,
    memberEntity: MemberNameEntity,
    selected: Boolean,
    selectMember: (MemberNameEntity) -> Unit,
    unselectMember: (MemberNameEntity) -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = BgF8F8FA,
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp, bottom = 15.dp, start = 20.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    modifier = Modifier.size(40.dp),
                    painter = painterResource(id = R.drawable.ic_member_profile),
                    contentDescription = null,
                    tint = Color.Unspecified
                )

                Text(text = memberEntity.name, style = Typography.body2)
            }

            CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                IconButton(onClick = {
                    if (selected) unselectMember(memberEntity) else selectMember(
                        memberEntity
                    )
                }) {
                    Icon(
                        painter = painterResource(id = if (selected) R.drawable.ic_circle_remove else R.drawable.ic_circle_add),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                }
            }
        }
    }
}