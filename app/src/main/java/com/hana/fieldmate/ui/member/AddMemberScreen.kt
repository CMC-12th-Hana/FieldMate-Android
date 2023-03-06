package com.hana.fieldmate.ui.member

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hana.fieldmate.R
import com.hana.fieldmate.ui.auth.Label
import com.hana.fieldmate.ui.component.FAppBarWithBackBtn
import com.hana.fieldmate.ui.component.FButton
import com.hana.fieldmate.ui.component.FDialog
import com.hana.fieldmate.ui.component.FTextField
import com.hana.fieldmate.ui.theme.*

@Composable
fun AddMemberScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    confirmBtnOnClick: () -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var rank by rememberSaveable { mutableStateOf("") }
    var number by rememberSaveable { mutableStateOf("") }

    var addMemberAlertDialogOpen by rememberSaveable { mutableStateOf(false) }

    if (addMemberAlertDialogOpen) AddMemberAlertDialog(
        memberName = name,
        companyName = "회사 이름",
        onClose = { addMemberAlertDialogOpen = false }
    )

    Scaffold(
        topBar = {
            FAppBarWithBackBtn(
                title = stringResource(id = R.string.add_member),
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

                Label(text = stringResource(id = R.string.member_name))
                Spacer(modifier = Modifier.height(8.dp))
                FTextField(
                    modifier = Modifier.fillMaxWidth(),
                    msgContent = name,
                    hint = stringResource(id = R.string.member_name_hint),
                    onValueChange = { name = it })

                Spacer(modifier = Modifier.height(25.dp))

                Label(text = stringResource(id = R.string.member_phone))
                Spacer(modifier = Modifier.height(8.dp))
                FTextField(
                    modifier = Modifier.fillMaxWidth(),
                    msgContent = phone,
                    hint = stringResource(id = R.string.member_phone_hint),
                    onValueChange = { phone = it }
                )

                Spacer(modifier = Modifier.height(25.dp))

                Label(text = stringResource(id = R.string.member_phone))
                Spacer(modifier = Modifier.height(8.dp))
                FTextField(
                    modifier = Modifier.fillMaxWidth(),
                    msgContent = phone,
                    hint = stringResource(id = R.string.member_phone_hint),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone
                    ),
                    onValueChange = { phone = it }
                )

                Spacer(modifier = Modifier.height(25.dp))

                Label(text = stringResource(id = R.string.member_rank))
                Spacer(modifier = Modifier.height(8.dp))
                FTextField(
                    modifier = Modifier.fillMaxWidth(),
                    msgContent = rank,
                    hint = stringResource(id = R.string.member_rank_hint),
                    onValueChange = { rank = it }
                )

                Spacer(modifier = Modifier.height(25.dp))

                Label(text = stringResource(id = R.string.member_number))
                Spacer(modifier = Modifier.height(8.dp))
                FTextField(
                    modifier = Modifier.fillMaxWidth(),
                    msgContent = number,
                    hint = stringResource(id = R.string.member_number_hint),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    onValueChange = { number = it }
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                )

                Column {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = Shapes.large,
                        color = Font191919.copy(alpha = 0.7f),
                        elevation = 0.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 15.dp, bottom = 15.dp, start = 20.dp, end = 20.dp),
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

                    Spacer(modifier = Modifier.height(20.dp))

                    FButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.complete),
                        onClick = {
                            addMemberAlertDialogOpen = true
                            confirmBtnOnClick()
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
fun AddMemberAlertDialog(
    modifier: Modifier = Modifier,
    memberName: String,
    companyName: String,
    onClose: () -> Unit
) {
    FDialog(
        onDismissRequest = { },
        content = {
            Text(
                modifier = Modifier.padding(top = 30.dp, bottom = 30.dp),
                text = "이제 ${memberName}님은 ${companyName}의 구성원입니다!\n함께 업무 관리를 시작해보세요 ",
                textAlign = TextAlign.Center,
                style = Typography.body2
            )
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