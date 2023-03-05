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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hana.fieldmate.FieldMateScreen
import com.hana.fieldmate.R
import com.hana.fieldmate.domain.model.MemberEntity
import com.hana.fieldmate.ui.component.FBottomBar
import com.hana.fieldmate.ui.component.FSearchTextField
import com.hana.fieldmate.ui.theme.*

@Composable
fun MemberScreen(
    modifier: Modifier = Modifier,
    uiState: MemberListUiState,
    navController: NavController
) {
    var memberKeyword by rememberSaveable { mutableStateOf("") }

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
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)) {
                    Spacer(modifier = Modifier.height(20.dp))

                    FSearchTextField(
                        modifier = Modifier.fillMaxWidth(),
                        msgContent = memberKeyword,
                        hint = stringResource(id = R.string.search_member_hint),
                        onValueChange = { memberKeyword = it }
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            MemberListContent(
                memberEntityList = uiState.memberEntityList,
                navController = navController
            )
        }
    }
}

@Composable
fun MemberListContent(
    modifier: Modifier = Modifier,
    memberEntityList: List<MemberEntity>,
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
                memberEntity = MemberEntity(
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

        items(memberEntityList) { member ->
            MemberItem(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    navController.navigate("${FieldMateScreen.DetailMember.name}/${member.id}")
                },
                memberEntity = member
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
                    painter = painterResource(id = memberEntity.profileImg),
                    contentDescription = null,
                    tint = Color.Unspecified
                )

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

@Preview(showBackground = true)
@Composable
fun PreviewMemberScreen() {
    FieldMateTheme {
        MemberScreen(uiState = MemberListUiState(), navController = rememberNavController())
    }
}