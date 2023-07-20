package com.hana.fieldmate.ui.member

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hana.fieldmate.App
import com.hana.fieldmate.R
import com.hana.fieldmate.data.ErrorType
import com.hana.fieldmate.ui.DialogEvent
import com.hana.fieldmate.ui.component.*
import com.hana.fieldmate.ui.member.viewmodel.MemberViewModel
import com.hana.fieldmate.ui.navigation.NavigateActions
import com.hana.fieldmate.ui.theme.Font70747E
import com.hana.fieldmate.ui.theme.Typography
import com.hana.fieldmate.ui.theme.body4
import com.hana.fieldmate.util.LEADER
import com.hana.fieldmate.util.UPDATE_PHONE_NUMBER_MESSAGE

@Composable
fun EditMemberScreen(
    modifier: Modifier = Modifier,
    viewModel: MemberViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val userInfo = App.getInstance().getUserInfo()
    val member = uiState.member

    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var staffRank by remember { mutableStateOf("") }
    var staffNumber by remember { mutableStateOf("") }

    when (uiState.dialog) {
        is DialogEvent.Confirm -> {
            BackToLoginDialog(
                onClose = { viewModel.backToLogin() },
                message = UPDATE_PHONE_NUMBER_MESSAGE
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

    LaunchedEffect(member) {
        name = member.name
        phoneNumber = member.phoneNumber
        staffRank = member.staffRank
        staffNumber = member.staffNumber
    }

    LaunchedEffect(true) {
        viewModel.loadMember()
    }

    Scaffold(
        topBar = {
            FAppBarWithBackBtn(
                title = stringResource(id = R.string.edit_profile),
                backBtnOnClick = {
                    viewModel.navigateTo(NavigateActions.navigateUp())
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            LoadingContent(uiState.memberLoadingState) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(40.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        val context = LocalContext.current

                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(member.profileImg)
                                .build(),
                            modifier = Modifier.size(70.dp),
                            filterQuality = FilterQuality.Low,
                            contentScale = ContentScale.Crop,
                            contentDescription = null
                        )
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.name),
                        style = Typography.body4
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    FTextField(
                        modifier = Modifier.fillMaxWidth(),
                        msgContent = name,
                        onValueChange = { name = it }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.phone),
                        style = Typography.body4
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    FTextField(
                        modifier = Modifier.fillMaxWidth(),
                        msgContent = phoneNumber,
                        enabled = userInfo.userRole == LEADER,
                        onValueChange = { phoneNumber = it }
                    )

                    if (userInfo.userRole != "리더") {
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_info),
                                tint = Color.Black,
                                contentDescription = null
                            )

                            Text(
                                text = stringResource(id = R.string.change_phone_info),
                                style = Typography.body4,
                                color = Font70747E
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.member_rank),
                        style = Typography.body4
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    FTextField(
                        modifier = Modifier.fillMaxWidth(),
                        msgContent = staffRank,
                        onValueChange = { staffRank = it }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.member_number),
                        style = Typography.body4
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    FTextField(
                        modifier = Modifier.fillMaxWidth(),
                        msgContent = staffNumber,
                        onValueChange = { staffNumber = it }
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
            Column {
                Spacer(modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f))

                Spacer(modifier = Modifier.height(40.dp))

                FButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp),
                    text = stringResource(id = R.string.edit_complete),
                    onClick = {
                        if (phoneNumber.matches("""^01([016789])-?([0-9]{3,4})-?([0-9]{4})$""".toRegex())) {
                            if (userInfo.userRole == LEADER) {
                                viewModel.updateMemberProfile(
                                    name,
                                    phoneNumber,
                                    staffNumber,
                                    staffRank
                                )
                            }
                            else {
                                viewModel.updateMyProfile(name, staffNumber, staffRank)
                            }
                        } else {
                            viewModel.openPhoneNumberErrorDialog()
                        }
                    }
                )

                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }
}
