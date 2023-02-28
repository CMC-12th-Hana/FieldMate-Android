package com.hana.umuljeong.ui.report

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.accompanist.flowlayout.FlowRow
import com.hana.umuljeong.R
import com.hana.umuljeong.data.datasource.fakeBussinessSelectionData
import com.hana.umuljeong.data.datasource.fakeCategorySelectionData
import com.hana.umuljeong.ui.component.UAppBarWithBackBtn
import com.hana.umuljeong.ui.component.USearchTextField
import com.hana.umuljeong.ui.theme.Font70747E
import com.hana.umuljeong.ui.theme.LineDBDBDB
import com.hana.umuljeong.ui.theme.Shapes
import com.hana.umuljeong.ui.theme.Typography

enum class SearchMode {
    COMPANY, BUSINESS
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchDialog(
    modifier: Modifier = Modifier,
    mode: SearchMode,
    onSelected: (String) -> Unit,
    onClosed: () -> Unit,
) {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Scaffold(
            topBar = {
                UAppBarWithBackBtn(
                    title = stringResource(id = if (mode == SearchMode.COMPANY) R.string.search_client_name else R.string.search_business_name),
                    backBtnOnClick = {
                        onClosed()
                    }
                )
            }
        ) { innerPadding ->
            var keyword by rememberSaveable { mutableStateOf("") }
            var searchBarFocus by rememberSaveable { mutableStateOf(false) }

            BackHandler {
                onClosed()
            }

            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Column(
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(20.dp))

                    USearchTextField(
                        modifier = Modifier.fillMaxWidth(),
                        msgContent = keyword,
                        hint = stringResource(id = if (mode == SearchMode.COMPANY) R.string.search_client_hint else R.string.search_business_hint),
                        onValueChange = { keyword = it },
                        onFocusChange = { searchBarFocus = it }
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    if (!searchBarFocus) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, end = 20.dp),
                            text = stringResource(id = R.string.recently_added),
                            style = Typography.body1
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, end = 20.dp),
                            crossAxisSpacing = 5.dp,
                            mainAxisSpacing = 5.dp
                        ) {
                            val keywords = listOf(
                                "디지털코리아 전용회선",
                                "Central Makeus Challenge",
                                "하나",
                                "Field Mate",
                                "SW 마에스트로 코테 붙고싶다"
                            )

                            for (i in keywords) KeywordItem(
                                keyword = i,
                                onClick = { onSelected(i) })
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, end = 20.dp)
                        ) {
                            val data =
                                if (mode == SearchMode.COMPANY) fakeCategorySelectionData else fakeBussinessSelectionData

                            val filteredData =
                                data.filter { it.contains(keyword, ignoreCase = true) }
                            if (filteredData.isNotEmpty()) {
                                items(filteredData) {
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable(onClick = { onSelected(it) })
                                            .padding(top = 10.dp, bottom = 10.dp),
                                        text = it,
                                        style = Typography.body2,
                                        color = Font70747E
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun KeywordItem(
    modifier: Modifier = Modifier,
    keyword: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier,
        onClick = onClick,
        color = Color.White,
        elevation = 0.dp,
        shape = Shapes.large,
        border = BorderStroke(1.dp, LineDBDBDB)
    ) {
        Row(
            modifier = Modifier.padding(top = 6.dp, bottom = 6.dp, start = 12.dp, end = 12.dp)
        ) {
            Text(text = keyword, style = Typography.body2, color = Font70747E)
        }
    }
}