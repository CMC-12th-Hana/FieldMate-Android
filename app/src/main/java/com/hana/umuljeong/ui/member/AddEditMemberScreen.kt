package com.hana.umuljeong.ui.member

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hana.umuljeong.R
import com.hana.umuljeong.ui.component.UAppBarWithBackBtn
import com.hana.umuljeong.ui.component.UButton
import com.hana.umuljeong.ui.component.UTextField
import com.hana.umuljeong.ui.theme.Font70747E
import com.hana.umuljeong.ui.theme.Typography
import com.hana.umuljeong.ui.theme.body4

@Composable
fun AddEditMemberScreen(
    modifier: Modifier = Modifier,
    uiState: MemberUiState,
    navController: NavController,
    confirmBtnOnClick: () -> Unit
) {
    val member = uiState.memberEntity
    var name by rememberSaveable { mutableStateOf(member.name) }
    var phone by rememberSaveable { mutableStateOf(member.phone) }
    var memberNum by rememberSaveable { mutableStateOf(member.memberNum) }

    Scaffold(
        topBar = {
            UAppBarWithBackBtn(
                title = stringResource(id = R.string.edit_profile),
                backBtnOnClick = {
                    navController.navigateUp()
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
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
                    Icon(
                        modifier = Modifier.size(70.dp),
                        painter = painterResource(id = member.profileImg),
                        tint = Color.Unspecified,
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

                UTextField(
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

                UTextField(
                    modifier = Modifier.fillMaxWidth(),
                    msgContent = phone,
                    enabled = false,
                    readOnly = true,
                    onValueChange = { phone = it }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_info),
                        tint = Color.Unspecified,
                        contentDescription = null
                    )

                    Text(
                        text = stringResource(id = R.string.change_phone_info),
                        style = Typography.body4,
                        color = Font70747E
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.member_number),
                    style = Typography.body4
                )

                Spacer(modifier = Modifier.height(8.dp))

                UTextField(
                    modifier = Modifier.fillMaxWidth(),
                    msgContent = memberNum,
                    onValueChange = { memberNum = it }
                )

                Spacer(modifier = Modifier.height(20.dp))
            }
            Column {
                Spacer(modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f))

                Spacer(modifier = Modifier.height(40.dp))

                UButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp),
                    text = stringResource(id = R.string.edit_complete),
                    onClick = confirmBtnOnClick
                )

                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }
}
