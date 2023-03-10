package com.hana.fieldmate.ui.business

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.hana.fieldmate.R
import com.hana.fieldmate.domain.model.MemberNameEntity
import com.hana.fieldmate.ui.component.FAppBarWithBackBtn
import com.hana.fieldmate.ui.component.FButton
import com.hana.fieldmate.ui.component.FSearchTextField
import com.hana.fieldmate.ui.theme.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SelectMemberDialog(
    companyMembers: List<MemberNameEntity>,
    selectedMemberList: List<MemberNameEntity>,
    selectMember: (MemberNameEntity) -> Unit,
    unselectMember: (MemberNameEntity) -> Unit,
    onSelect: () -> Unit,
    onClosed: () -> Unit
) {
    BackHandler {
        onClosed()
    }

    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Scaffold(
            topBar = {
                FAppBarWithBackBtn(
                    title = stringResource(id = R.string.add_members),
                    backBtnOnClick = {
                        onClosed()
                    }
                )
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyColumn(
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(20.dp))

                        var memberKeyword by rememberSaveable { mutableStateOf("") }
                        FSearchTextField(
                            modifier = Modifier.fillMaxWidth(),
                            msgContent = memberKeyword,
                            hint = stringResource(id = R.string.search_member_hint),
                            onValueChange = { memberKeyword = it }
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    items(companyMembers) { member ->
                        SelectableMemberItem(
                            modifier = Modifier.fillMaxWidth(),
                            memberEntity = member,
                            selected = selectedMemberList.contains(member),
                            selectMember = selectMember,
                            unselectMember = unselectMember
                        )
                    }
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                )

                Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp)) {
                    Spacer(Modifier.height(40.dp))

                    FButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp),
                        text = stringResource(id = R.string.complete),
                        onClick = { onSelect() }
                    )

                    Spacer(Modifier.height(50.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SelectableMemberItem(
    modifier: Modifier = Modifier,
    shape: Shape = Shapes.large,
    memberEntity: MemberNameEntity,
    selected: Boolean,
    selectMember: (MemberNameEntity) -> Unit,
    unselectMember: (MemberNameEntity) -> Unit,
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
                    painter = painterResource(id = R.drawable.ic_member_profile),
                    contentDescription = null,
                    tint = Color.Unspecified
                )

                Text(text = memberEntity.name, style = Typography.body2)
            }

            CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                IconButton(onClick = {
                    if (selected) unselectMember(memberEntity) else selectMember(
                        memberEntity
                    )
                }) {
                    Icon(
                        painter = painterResource(id = if (selected) R.drawable.ic_circle_remove else R.drawable.ic_circle_add),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                }
            }
        }
    }
}