package com.hana.fieldmate.ui.client

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hana.fieldmate.FieldMateScreen
import com.hana.fieldmate.R
import com.hana.fieldmate.data.local.fakeClientDataSource
import com.hana.fieldmate.domain.model.ClientEntity
import com.hana.fieldmate.toFormattedPhoneNum
import com.hana.fieldmate.ui.component.*
import com.hana.fieldmate.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ClientScreen(
    modifier: Modifier = Modifier,
    uiState: ClientListUiState,
    addBtnOnClick: () -> Unit,
    navController: NavController
) {
    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded },
        skipHalfExpanded = true
    )

    var clientName by rememberSaveable { mutableStateOf("") }

    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetShape = RoundedCornerShape(
            topStart = 12.dp,
            topEnd = 12.dp
        ),
        sheetBackgroundColor = Color.White,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, end = 4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 15.dp),
                    text = stringResource(id = R.string.sort),
                    style = Typography.body1,
                    textAlign = TextAlign.Center
                )

                Surface(
                    onClick = {
                        coroutineScope.launch {
                            modalSheetState.animateTo(ModalBottomSheetValue.Hidden)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.padding(
                            top = 15.dp,
                            bottom = 15.dp,
                            start = 10.dp,
                            end = 10.dp
                        ),
                        text = stringResource(id = R.string.visit_desc), style = Typography.body2
                    )
                }

                Surface(
                    onClick = {
                        coroutineScope.launch {
                            modalSheetState.animateTo(ModalBottomSheetValue.Hidden)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.padding(
                            top = 15.dp,
                            bottom = 15.dp,
                            start = 10.dp,
                            end = 10.dp
                        ),
                        text = stringResource(id = R.string.visit_asc), style = Typography.body2
                    )
                }

                Surface(
                    onClick = {
                        coroutineScope.launch {
                            modalSheetState.animateTo(ModalBottomSheetValue.Hidden)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.padding(
                            top = 15.dp,
                            bottom = 15.dp,
                            start = 10.dp,
                            end = 10.dp
                        ),
                        text = stringResource(id = R.string.business_desc), style = Typography.body2
                    )
                }

                Surface(
                    onClick = {
                        coroutineScope.launch {
                            modalSheetState.animateTo(ModalBottomSheetValue.Hidden)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.padding(
                            top = 15.dp,
                            bottom = 15.dp,
                            start = 10.dp,
                            end = 10.dp
                        ),
                        text = stringResource(id = R.string.business_asc), style = Typography.body2
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    ) {
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
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp)
                    ) {
                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(15.dp)
                        ) {
                            FSearchTextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                msgContent = clientName,
                                hint = stringResource(id = R.string.search_client_hint),
                                onValueChange = { clientName = it }
                            )

                            CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                                IconButton(
                                    onClick = {
                                        coroutineScope.launch {
                                            modalSheetState.animateTo(ModalBottomSheetValue.Expanded)
                                        }
                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_sort),
                                        tint = Color.Unspecified,
                                        contentDescription = null
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }

                ClientContent(
                    clientEntityList = uiState.clientEntityList,
                    navController = navController,
                    addBtnOnClick = addBtnOnClick
                )
            }
        }
    }
}

@Composable
fun ClientContent(
    modifier: Modifier = Modifier,
    clientEntityList: List<ClientEntity>,
    navController: NavController,
    addBtnOnClick: () -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(20.dp))

            FAddButton(
                onClick = addBtnOnClick,
                text = stringResource(id = R.string.add_client),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))
        }

        items(clientEntityList) { client ->
            ClientItem(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    navController.navigate("${FieldMateScreen.DetailClient.name}/${client.id}")
                },
                clientEntity = client
            )
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ClientItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    shape: Shape = Shapes.large,
    clientEntity: ClientEntity
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        color = BgF8F8FA,
        elevation = 0.dp
    ) {
        Column(
            modifier = Modifier.padding(all = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row {
                            Text(
                                modifier = Modifier
                                    .background(color = Main356DF8, shape = shapes.small)
                                    .padding(top = 3.dp, bottom = 3.dp, start = 8.dp, end = 8.dp),
                                text = stringResource(id = R.string.total_work_number) + " ${clientEntity.taskCount}",
                                style = Typography.body6,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                modifier = Modifier
                                    .background(color = Color.Transparent, shape = shapes.small)
                                    .border(width = 1.dp, color = Color(0xFFBECCE9))
                                    .padding(top = 3.dp, bottom = 3.dp, start = 8.dp, end = 8.dp),
                                text = stringResource(id = R.string.business_number) + " ${clientEntity.businessCount}",
                                style = Typography.body6,
                                color = Main356DF8
                            )
                        }

                        val context = LocalContext.current

                        CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                            IconButton(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_DIAL).apply {
                                        data = Uri.parse(clientEntity.phone.toFormattedPhoneNum())
                                    }
                                    context.startActivity(intent)
                                }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_call),
                                    tint = Color.Unspecified,
                                    contentDescription = null
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = clientEntity.name,
                        style = Typography.body1
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewCompany() {
    FieldMateTheme {
        ClientItem(onClick = { }, clientEntity = fakeClientDataSource[0])
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCompanyScreen() {
    FieldMateTheme {
        ClientScreen(
            uiState = ClientListUiState(),
            addBtnOnClick = { },
            navController = rememberNavController()
        )
    }
}