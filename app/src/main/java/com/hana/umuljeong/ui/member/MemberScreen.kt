package com.hana.umuljeong.ui.member

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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hana.umuljeong.R
import com.hana.umuljeong.UmuljeongScreen
import com.hana.umuljeong.domain.Member
import com.hana.umuljeong.ui.component.UBottomBar
import com.hana.umuljeong.ui.component.USearchTextField
import com.hana.umuljeong.ui.theme.*

@Composable
fun MemberScreen(
    modifier: Modifier = Modifier,
    uiState: MemberListUiState,
    navController: NavController
) {
    var memberKeyword by rememberSaveable { mutableStateOf("") }

    Scaffold(
        bottomBar = {
            UBottomBar(
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
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)) {
                    Spacer(modifier = Modifier.height(20.dp))

                    USearchTextField(
                        modifier = Modifier.fillMaxWidth(),
                        msgContent = memberKeyword,
                        hint = stringResource(id = R.string.search_member_hint),
                        onValueChange = { memberKeyword = it }
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            MemberListContent(
                memberList = uiState.memberList,
                navController = navController
            )
        }
    }
}

@Composable
fun MemberListContent(
    modifier: Modifier = Modifier,
    memberList: List<Member>,
    navController: NavController
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(15.dp))
            MemberItem(
                modifier = Modifier.fillMaxWidth(),
                onClick = { },
                member = Member(
                    id = 99,
                    name = "나",
                    profileImg = R.drawable.ic_my_profile,
                    company = "",
                    phone = "",
                    grade = "",
                    memberNum = ""
                )
            )
            Spacer(modifier = Modifier.height(15.dp))
        }

        items(memberList) { member ->
            MemberItem(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    navController.navigate("${UmuljeongScreen.DetailMember.name}/${member.id}")
                },
                member = member
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
    member: Member
) {
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
                    painter = painterResource(id = member.profileImg),
                    contentDescription = null,
                    tint = Color.Unspecified
                )

                Text(text = member.name, style = Typography.body3)
            }


            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMemberScreen() {
    UmuljeongTheme {
        MemberScreen(uiState = MemberListUiState(), navController = rememberNavController())
    }
}