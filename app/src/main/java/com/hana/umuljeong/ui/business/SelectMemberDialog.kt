package com.hana.umuljeong.ui.business

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
import com.hana.umuljeong.R
import com.hana.umuljeong.data.remote.datasource.fakeMemberDataSource
import com.hana.umuljeong.domain.model.MemberEntity
import com.hana.umuljeong.ui.component.UAppBarWithBackBtn
import com.hana.umuljeong.ui.component.UButton
import com.hana.umuljeong.ui.component.USearchTextField
import com.hana.umuljeong.ui.theme.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SelectMemberDialog(
    modifier: Modifier = Modifier,
    onSelected: (List<MemberEntity>) -> Unit,
    onClosed: () -> Unit
) {
    val selectedMemberListEntity = remember { mutableStateListOf<MemberEntity>() }

    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Scaffold(
            topBar = {
                UAppBarWithBackBtn(
                    title = stringResource(id = R.string.add_members),
                    backBtnOnClick = {
                        onClosed()
                    }
                )
            },
        ) { innerPadding ->
            Column(
                modifier = modifier
                    .fillMaxSize()
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

                    items(fakeMemberDataSource) { member ->
                        SelectableMemberItem(
                            modifier = Modifier.fillMaxWidth(),
                            memberEntity = member,
                            selected = selectedMemberListEntity.contains(member),
                            selectMember = { selectedMemberListEntity.add(member) },
                            unselectMember = { selectedMemberListEntity.remove(member) }
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

                    UButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp),
                        text = stringResource(id = R.string.complete),
                        onClick = { onSelected(selectedMemberListEntity) }
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
    memberEntity: MemberEntity,
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
                painter = painterResource(id = memberEntity.profileImg),
                contentDescription = null,
                tint = Color.Unspecified
            )

            Text(text = memberEntity.name, style = Typography.body3)
        }
    }
}