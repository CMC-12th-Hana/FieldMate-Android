package com.hana.fieldmate.ui.setting

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hana.fieldmate.App
import com.hana.fieldmate.R
import com.hana.fieldmate.data.ErrorType
import com.hana.fieldmate.domain.model.MemberEntity
import com.hana.fieldmate.ui.DialogEvent
import com.hana.fieldmate.ui.component.*
import com.hana.fieldmate.ui.navigation.NavigateActions
import com.hana.fieldmate.ui.setting.viewmodel.ChangeLeaderViewModel
import com.hana.fieldmate.ui.theme.*

@Composable
fun ChangeLeaderScreen(
    modifier: Modifier = Modifier,
    viewModel: ChangeLeaderViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val userInfo = App.getInstance().getUserInfo()
    val memberList: List<MemberEntity> = uiState.memberList

    var selectedMemberId by remember { mutableStateOf(-1L) }

    var memberName by remember { mutableStateOf("") }
    var selectedName by remember { mutableStateOf("") }

    when (uiState.dialog) {
        is DialogEvent.Select -> {
            SelectLeaderDialog(
                memberName = memberList.find { it.id == selectedMemberId }?.name ?: "",
                onClose = { viewModel.onDialogClosed() },
                onConfirm = { viewModel.updateMemberToLeader(selectedMemberId) }
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

    LaunchedEffect(selectedName) {
        viewModel.loadMembers(userInfo.companyId, selectedName)
    }

    LaunchedEffect(true) {
        viewModel.loadMembers(userInfo.companyId, null)
    }

    Scaffold(
        topBar = {
            FAppBarWithBackBtn(
                title = stringResource(id = R.string.change_leader),
                backBtnOnClick = {
                    viewModel.navigateTo(NavigateActions.navigateUp())
                }
            )
        }
    ) { innerPadding ->
        LoadingContent(loadingState = uiState.memberListLoadingState) {
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

                    items(memberList.filter { userInfo.userId != it.id }) { member ->
                        RadioButtonMemberItem(
                            modifier = Modifier.fillMaxWidth(),
                            memberEntity = member,
                            selected = member.id == selectedMemberId,
                            selectMember = {
                                selectedMemberId = it.id
                            }
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }

                Spacer(modifier = Modifier.fillMaxHeight())

                Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp)) {
                    Spacer(Modifier.height(20.dp))

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
                            Icon(
                                painter = painterResource(id = R.drawable.ic_info),
                                tint = Color.White,
                                contentDescription = null
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = stringResource(id = R.string.leader_permission_info),
                                style = Typography.body3,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(Modifier.height(30.dp))

                    FButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.complete),
                        onClick = {
                            viewModel.openSelectLeaderDialog()
                        }
                    )

                    Spacer(Modifier.height(50.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RadioButtonMemberItem(
    modifier: Modifier = Modifier,
    shape: Shape = Shapes.large,
    memberEntity: MemberEntity,
    selected: Boolean,
    selectMember: (MemberEntity) -> Unit
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
                CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                    RadioButton(
                        modifier = Modifier.size(24.dp),
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Main356DF8
                        ),
                        selected = selected,
                        onClick = {
                            if (!selected) selectMember(memberEntity)
                        }
                    )
                }

                Icon(
                    modifier = Modifier.size(40.dp),
                    painter = painterResource(id = R.drawable.ic_member_profile),
                    contentDescription = null,
                    tint = Color.Unspecified
                )

                Text(text = memberEntity.name, style = Typography.body2)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SelectLeaderDialog(
    modifier: Modifier = Modifier,
    memberName: String,
    onClose: () -> Unit,
    onConfirm: () -> Unit
) {
    FDialog(
        onDismissRequest = { },
        content = {
            Column(
                modifier = Modifier.padding(all = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row {
                    Text(
                        text = "리더권한을 ",
                        textAlign = TextAlign.Center,
                        style = Typography.body2
                    )
                    Text(
                        text = memberName,
                        textAlign = TextAlign.Center,
                        style = Typography.body2,
                        color = Main356DF8
                    )
                    Text(
                        text = "에게",
                        textAlign = TextAlign.Center,
                        style = Typography.body2
                    )
                }
                Row {
                    Text(
                        text = "넘기겠습니까?",
                        textAlign = TextAlign.Center,
                        style = Typography.body2
                    )
                }
            }
        },
        button = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    onClick = onClose
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(id = R.string.cancel),
                            style = Typography.body1,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(
                    Modifier
                        .width(1.dp)
                        .fillMaxHeight()
                        .background(LineDBDBDB)
                )

                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    onClick = onConfirm
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(id = R.string.confirm),
                            style = Typography.body1,
                            textAlign = TextAlign.Center,
                            color = Main356DF8
                        )
                    }
                }
            }
        }
    )
}