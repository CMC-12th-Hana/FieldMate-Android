package com.hana.fieldmate.ui.business

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.hana.fieldmate.R
import com.hana.fieldmate.domain.model.MemberNameEntity
import com.hana.fieldmate.ui.component.FAppBarWithBackBtn
import com.hana.fieldmate.ui.component.FButton
import com.hana.fieldmate.ui.component.FSearchTextField

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddBusinessMemberDialog(
    companyMembers: List<MemberNameEntity>,
    selectedMemberList: List<MemberNameEntity>,
    selectMember: (MemberNameEntity) -> Unit,
    unselectMember: (MemberNameEntity) -> Unit,
    onSearch: (String) -> Unit,
    onSelect: () -> Unit,
    onClosed: () -> Unit
) {
    BackHandler {
        onClosed()
    }

    var memberName by remember { mutableStateOf("") }

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

                        FSearchTextField(
                            modifier = Modifier.fillMaxWidth(),
                            msgContent = memberName,
                            hint = stringResource(id = R.string.search_member_hint),
                            onSearch = { onSearch(it) },
                            onValueChange = { memberName = it }
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

                Column {
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