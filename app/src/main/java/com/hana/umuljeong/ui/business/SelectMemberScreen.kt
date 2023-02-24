package com.hana.umuljeong.ui.business

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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hana.umuljeong.R
import com.hana.umuljeong.data.datasource.fakeMemberData
import com.hana.umuljeong.domain.Member
import com.hana.umuljeong.ui.component.UAppBarWithBackBtn
import com.hana.umuljeong.ui.component.UButton
import com.hana.umuljeong.ui.component.USearchTextField
import com.hana.umuljeong.ui.theme.*

@Composable
fun SelectMemberScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    onSelected: (List<Member>) -> Unit
) {
    var selectedMembers = remember { mutableStateListOf<Member>() }

    Scaffold(
        topBar = {
            UAppBarWithBackBtn(
                title = stringResource(id = R.string.member_profile),
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
            LazyColumn(
                modifier = modifier.padding(start = 20.dp, end = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(20.dp))

                    var memberKeyword by rememberSaveable { mutableStateOf("") }
                    USearchTextField(
                        modifier = Modifier.fillMaxWidth(),
                        msgContent = memberKeyword,
                        hint = stringResource(id = R.string.search_member_hint),
                        onValueChange = { memberKeyword = it }
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }

                items(fakeMemberData) { member ->
                    SelectableMemberItem(
                        modifier = Modifier.fillMaxWidth(),
                        member = member,
                        selected = selectedMembers.contains(member),
                        selectMember = { selectedMembers.add(member) },
                        unselectMember = { selectedMembers.remove(member) }
                    )
                }
            }

            Spacer(modifier = Modifier
                .fillMaxHeight()
                .weight(1f))

            Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp)) {
                Spacer(Modifier.height(40.dp))

                UButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp),
                    text = stringResource(id = R.string.complete),
                    onClick = { onSelected(selectedMembers) }
                )

                Spacer(Modifier.height(50.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SelectableMemberItem(
    modifier: Modifier = Modifier,
    shape: Shape = Shapes.large,
    member: Member,
    selected: Boolean,
    selectMember: () -> Boolean,
    unselectMember: () -> Boolean,
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
                .padding(top = 15.dp, bottom = 15.dp, start = 25.dp, end = 25.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                RadioButton(
                    modifier = Modifier.size(24.dp),
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Main356DF8
                    ),
                    selected = selected,
                    onClick = {
                        if (!selected) selectMember() else unselectMember()
                    }
                )
            }

            Icon(
                painter = painterResource(id = member.profileImg),
                contentDescription = null,
                tint = Color.Unspecified
            )

            Text(text = member.name, style = Typography.body3)
        }
    }
}