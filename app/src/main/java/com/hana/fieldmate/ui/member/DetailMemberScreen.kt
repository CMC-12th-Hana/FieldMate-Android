package com.hana.fieldmate.ui.member

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hana.fieldmate.FieldMateScreen
import com.hana.fieldmate.R
import com.hana.fieldmate.ui.component.FAppBarWithDeleteBtn
import com.hana.fieldmate.ui.component.FButton
import com.hana.fieldmate.ui.component.FDialog
import com.hana.fieldmate.ui.theme.*

@Composable
fun DetailMemberScreen(
    modifier: Modifier = Modifier,
    uiState: MemberUiState,
    navController: NavController
) {
    val member = uiState.memberEntity

    var deleteMemberDialogOpen by rememberSaveable { mutableStateOf(false) }

    if (deleteMemberDialogOpen) DeleteMemberDialog(
        onClose = {
            deleteMemberDialogOpen = false
        },
        onConfirm = {
            navController.navigateUp()
            deleteMemberDialogOpen = false
        }
    )

    Scaffold(
        topBar = {
            FAppBarWithDeleteBtn(
                title = stringResource(id = R.string.detail_profile),
                backBtnOnClick = {
                    navController.navigateUp()
                },
                deleteBtnOnClick = {
                    deleteMemberDialogOpen = true
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier.size(70.dp),
                    painter = painterResource(id = member.profileImg),
                    tint = Color.Unspecified,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = member.name,
                    style = Typography.title2
                )

                Spacer(modifier = Modifier.height(30.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    FButton(
                        onClick = { navController.navigate("${FieldMateScreen.EditMember.name}/${member.id}") },
                        shape = Shapes.medium,
                        text = stringResource(id = R.string.edit),
                        textStyle = Typography.body6,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.White
                        ),
                        border = BorderStroke(width = 1.dp, color = LineDBDBDB),
                        contentPadding = PaddingValues(
                            top = 6.dp,
                            bottom = 6.dp,
                            start = 8.dp,
                            end = 8.dp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    ComplainItem(
                        modifier = Modifier.fillMaxWidth(),
                        icon = painterResource(id = R.drawable.ic_profile_company),
                        title = stringResource(id = R.string.company_name),
                        description = member.company
                    )

                    ComplainItem(
                        modifier = Modifier.fillMaxWidth(),
                        icon = painterResource(id = R.drawable.ic_profile_call),
                        title = stringResource(id = R.string.member_phone),
                        description = member.phone
                    )

                    ComplainItem(
                        modifier = Modifier.fillMaxWidth(),
                        icon = painterResource(id = R.drawable.ic_grade),
                        title = stringResource(id = R.string.member_rank),
                        description = member.grade
                    )

                    ComplainItem(
                        modifier = Modifier.fillMaxWidth(),
                        icon = painterResource(id = R.drawable.ic_profile_mail),
                        title = stringResource(id = R.string.member_number),
                        description = member.memberNum
                    )
                }
            }
        }
    }
}

@Composable
fun ComplainItem(
    modifier: Modifier = Modifier,
    shape: Shape = Shapes.large,
    icon: Painter,
    title: String,
    description: String
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
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = icon,
                tint = Color.Unspecified,
                contentDescription = null
            )

            Text(
                text = title,
                style = Typography.body1,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = description,
                style = Typography.body2
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DeleteMemberDialog(
    modifier: Modifier = Modifier,
    onClose: () -> Unit,
    onConfirm: () -> Unit
) {
    FDialog(
        onDismissRequest = { },
        content = {
            Text(
                modifier = Modifier.padding(top = 30.dp, bottom = 30.dp),
                text = stringResource(id = R.string.delete_member_message),
                textAlign = TextAlign.Center,
                style = Typography.body2
            )
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
                    modifier
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
                            text = stringResource(id = R.string.delete),
                            style = Typography.body1,
                            textAlign = TextAlign.Center,
                            color = ErrorFF3120
                        )
                    }
                }
            }
        }
    )
}