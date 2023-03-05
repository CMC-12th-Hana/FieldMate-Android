package com.hana.fieldmate.ui.business

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hana.fieldmate.FieldMateScreen
import com.hana.fieldmate.R
import com.hana.fieldmate.data.local.fakeMemberDataSource
import com.hana.fieldmate.ui.component.FAppBarWithExitBtn
import com.hana.fieldmate.ui.member.MemberItem

@Composable
fun BusinessMemberScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Scaffold(
        topBar = {
            FAppBarWithExitBtn(
                title = stringResource(id = R.string.get_member_profile),
                exitBtnOnClick = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }

                items(fakeMemberDataSource) { member ->
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
    }
}