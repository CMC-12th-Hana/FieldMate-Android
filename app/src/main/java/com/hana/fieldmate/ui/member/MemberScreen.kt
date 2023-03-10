package com.hana.fieldmate.ui.member

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hana.fieldmate.FieldMateScreen
import com.hana.fieldmate.R
import com.hana.fieldmate.data.local.UserInfo
import com.hana.fieldmate.domain.model.MemberEntity
import com.hana.fieldmate.ui.DialogAction
import com.hana.fieldmate.ui.DialogState
import com.hana.fieldmate.ui.Event
import com.hana.fieldmate.ui.component.ErrorDialog
import com.hana.fieldmate.ui.component.FBottomBar
import com.hana.fieldmate.ui.component.FSearchTextField
import com.hana.fieldmate.ui.component.LoadingContent
import com.hana.fieldmate.ui.member.viewmodel.MemberListUiState
import com.hana.fieldmate.ui.theme.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MemberScreen(
    modifier: Modifier = Modifier,
    eventsFlow: Flow<Event>,
    sendEvent: (Event) -> Unit,
    loadMembers: (Long) -> Unit,
    uiState: MemberListUiState,
    userInfo: UserInfo,
    navController: NavController
) {
    var memberKeyword by rememberSaveable { mutableStateOf("") }

    var errorDialogOpen by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf("") }

    if (errorDialogOpen) ErrorDialog(
        errorMessage = errorMessage,
        onClose = { errorDialogOpen = false }
    )

    LaunchedEffect(true) {
        loadMembers(userInfo.companyId)

        eventsFlow.collectLatest { event ->
            when (event) {
                is Event.NavigateTo -> navController.navigate(event.destination)
                is Event.NavigatePopUpTo -> navController.navigate(event.destination) {
                    popUpTo(event.popUpDestination) {
                        inclusive = event.inclusive
                    }
                }
                is Event.NavigateUp -> navController.navigateUp()
                is Event.Dialog -> if (event.dialog == DialogState.Error) {
                    errorDialogOpen = event.action == DialogAction.Open
                    if (errorDialogOpen) errorMessage = event.description
                }
            }
        }
    }

    Scaffold(
        bottomBar = {
            FBottomBar(
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
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FSearchTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            msgContent = memberKeyword,
                            hint = stringResource(id = R.string.search_member_hint),
                            onValueChange = { memberKeyword = it }
                        )

                        if (userInfo.userRole == "??????") {
                            Spacer(modifier = Modifier.width(15.dp))

                            CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                                IconButton(onClick = { navController.navigate(FieldMateScreen.AddMember.name) }) {
                                    Icon(
                                        modifier = Modifier.size(24.dp),
                                        painter = painterResource(id = R.drawable.ic_circle_add),
                                        tint = Color.Unspecified,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            LoadingContent(loadingState = uiState.memberListLoadingState) {
                MemberListContent(
                    memberEntityList = uiState.memberEntityList,
                    userInfo = userInfo,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun MemberListContent(
    modifier: Modifier = Modifier,
    memberEntityList: List<MemberEntity>,
    userInfo: UserInfo,
    navController: NavController
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val myProfile = memberEntityList.find { it.id == userInfo.userId }!!

        item {
            Spacer(modifier = Modifier.height(20.dp))
            MyProfileItem(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    navController.navigate("${FieldMateScreen.DetailMember.name}/${myProfile.id}")
                },
                memberEntity = myProfile
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        items(memberEntityList.filter { it.id != userInfo.userId }) { member ->
            MemberItem(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    navController.navigate("${FieldMateScreen.DetailMember.name}/${member.id}")
                },
                memberEntity = member
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyProfileItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    shape: Shape = Shapes.large,
    memberEntity: MemberEntity
) {
    val context = LocalContext.current

    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        color = BgF8F8FA,
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 10.dp, start = 20.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(memberEntity.profileImg)
                        .build(),
                    modifier = Modifier.size(50.dp),
                    filterQuality = FilterQuality.Low,
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(text = memberEntity.name, style = Typography.body3)
            }


            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MemberItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    shape: Shape = Shapes.large,
    memberEntity: MemberEntity
) {
    val context = LocalContext.current

    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        color = BgF8F8FA,
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 10.dp, start = 20.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(memberEntity.profileImg)
                    .build(),
                modifier = Modifier.size(40.dp),
                filterQuality = FilterQuality.Low,
                contentScale = ContentScale.Crop,
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(text = memberEntity.name, style = Typography.body3)
        }
    }
}