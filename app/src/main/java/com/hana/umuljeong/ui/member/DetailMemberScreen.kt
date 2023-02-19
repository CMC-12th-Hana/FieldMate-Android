package com.hana.umuljeong.ui.member

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hana.umuljeong.R
import com.hana.umuljeong.UmuljeongScreen
import com.hana.umuljeong.data.model.Member
import com.hana.umuljeong.ui.component.UAppBarWithBackBtn
import com.hana.umuljeong.ui.component.UButton
import com.hana.umuljeong.ui.theme.*

@Composable
fun DetailMemberScreen(
    modifier: Modifier = Modifier,
    uiState: MemberUiState,
    navController: NavController
) {
    Scaffold(
        topBar = {
            UAppBarWithBackBtn(
                title = stringResource(id = R.string.detail_profile),
                backBtnOnClick = {
                    navController.navigateUp()
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            DetailMemberContent(member = uiState.member, navController = navController)
        }
    }
}

@Composable
fun DetailMemberContent(
    member: Member,
    navController: NavController
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

    Spacer(modifier = Modifier.height(5.dp))

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = "회사명",
            style = Typography.body3
        )

        Spacer(
            modifier = Modifier
                .width(1.dp)
                .height(12.dp)
                .background(FontDBDBDB)
        )

        Text(
            text = "사원",
            style = Typography.body3
        )
    }

    Spacer(modifier = Modifier.height(30.dp))

    Row(
        modifier = Modifier.width(335.dp),
        horizontalArrangement = Arrangement.End
    ) {
        UButton(
            onClick = { navController.navigate("${UmuljeongScreen.EditMember.name}/${member.id}") },
            shape = Shapes.medium,
            text = stringResource(id = R.string.edit),
            textStyle = Typography.body6,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.White
            ),
            border = BorderStroke(width = 1.dp, color = LineDBDBDB),
            contentPadding = PaddingValues(top = 6.dp, bottom = 6.dp, start = 8.dp, end = 8.dp)
        )
    }

    Spacer(modifier = Modifier.height(10.dp))

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        ComplainItem(
            modifier = Modifier.width(335.dp),
            icon = painterResource(id = R.drawable.ic_profile_call),
            description = member.phone
        )

        ComplainItem(
            modifier = Modifier.width(335.dp),
            icon = painterResource(id = R.drawable.ic_profile_mail),
            description = member.email
        )

        ComplainItem(
            modifier = Modifier.width(335.dp),
            icon = painterResource(id = R.drawable.ic_profile_number),
            description = member.phone
        )
    }
}

@Composable
fun ComplainItem(
    modifier: Modifier = Modifier,
    shape: Shape = Shapes.large,
    icon: Painter,
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
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Icon(
                painter = icon,
                tint = Color.Unspecified,
                contentDescription = null
            )

            Text(
                text = description,
                style = Typography.body3,
                color = Font70747E
            )
        }
    }
}