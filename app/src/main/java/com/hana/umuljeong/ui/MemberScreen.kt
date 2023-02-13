package com.hana.umuljeong.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import com.hana.umuljeong.data.datasource.fakeMemberData
import com.hana.umuljeong.data.model.Member
import com.hana.umuljeong.ui.component.UBottomBar
import com.hana.umuljeong.ui.component.USearchTextField
import com.hana.umuljeong.ui.theme.*

@Composable
fun MemberScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
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
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            var memberKeyword by remember { mutableStateOf("") }
            USearchTextField(
                modifier = Modifier.width(335.dp),
                msgContent = memberKeyword,
                hint = stringResource(id = R.string.search_member_hint),
                onValueChange = { memberKeyword = it }
            )

            Spacer(modifier = Modifier.height(15.dp))

            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(15.dp))
                    MemberItem(
                        modifier = Modifier.width(335.dp),
                        onClick = { },
                        member = Member(id = 99, name = "나", email = "", phone = "")
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                }

                items(fakeMemberData) { member ->
                    MemberItem(
                        modifier = Modifier.width(335.dp),
                        onClick = { },
                        member = member
                    )
                }
            }
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
        border = BorderStroke(width = 1.dp, color = BgD3D3D3),
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp, bottom = 15.dp, start = 25.dp, end = 25.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.img_member_profile),
                contentDescription = null,
                tint = Color.Unspecified
            )

            Text(text = member.name)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMemberScreen() {
    UmuljeongTheme {
        MemberScreen(navController = rememberNavController())
    }
}