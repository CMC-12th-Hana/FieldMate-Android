package com.hana.fieldmate.ui.business

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hana.fieldmate.App
import com.hana.fieldmate.R
import com.hana.fieldmate.data.ErrorType
import com.hana.fieldmate.ui.DialogEvent
import com.hana.fieldmate.ui.business.viewmodel.BusinessViewModel
import com.hana.fieldmate.ui.component.*
import com.hana.fieldmate.ui.navigation.NavigateActions
import com.hana.fieldmate.ui.theme.*
import com.hana.fieldmate.util.DateUtil.getShortenFormattedTime
import com.hana.fieldmate.util.LEADER
import java.text.NumberFormat
import java.util.*

@Composable
fun DetailBusinessScreen(
    modifier: Modifier = Modifier,
    viewModel: BusinessViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val userInfo = App.getInstance().getUserInfo()
    val business = uiState.business

    when (uiState.dialog) {
        is DialogEvent.Error -> {
            when (val error = (uiState.dialog as DialogEvent.Error).errorType) {
                is ErrorType.JwtExpired -> {
                    BackToLoginDialog(onClose = { viewModel.backToLogin() })
                }
                is ErrorType.General -> {
                    ErrorDialog(
                        errorMessage = error.errorMessage,
                        onClose = { viewModel.onDialogClosed() }
                    )
                }
            }
        }
        else -> {}
    }

    LaunchedEffect(true) {
        viewModel.loadBusiness()
    }

    Scaffold(
        topBar = {
            if (userInfo.userRole == LEADER) {
                FAppBarWithDeleteBtn(
                    title = stringResource(id = R.string.detail_business),
                    backBtnOnClick = {
                        viewModel.navigateTo(NavigateActions.navigateUp())
                    },
                    deleteBtnOnClick = {
                        viewModel.onDialogClosed()
                    }
                )
            } else {
                FAppBarWithBackBtn(
                    title = stringResource(id = R.string.detail_business),
                    backBtnOnClick = {
                        viewModel.navigateTo(NavigateActions.navigateUp())
                    }
                )
            }
        },
    ) { innerPadding ->
        Box(modifier = modifier.padding(innerPadding)) {
            LoadingContent(loadingState = uiState.businessLoadingState) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(start = 20.dp, end = 20.dp)
                ) {
                    Spacer(modifier = Modifier.height(30.dp))

                    Column(modifier = modifier.fillMaxWidth()) {
                        Row(
                            modifier = modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(BgF1F1F5),
                                painter = painterResource(id = R.drawable.ic_company),
                                tint = Color.Unspecified,
                                contentDescription = null
                            )

                            Spacer(modifier = Modifier.width(15.dp))

                            Text(
                                text = business.name,
                                style = Typography.title2
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Icon(
                                modifier = Modifier.clickable(
                                    onClick = {
                                        viewModel.navigateTo(
                                            NavigateActions.DetailBusinessScreen
                                                .toEditBusinessScreen(business.id)
                                        )
                                    }
                                ),
                                painter = painterResource(id = R.drawable.ic_gray_edit),
                                tint = Color.Unspecified,
                                contentDescription = null
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Column(
                            modifier = Modifier.padding(start = 10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    modifier = Modifier
                                        .background(
                                            color = Main356DF8,
                                            shape = MaterialTheme.shapes.small
                                        )
                                        .padding(
                                            top = 3.dp,
                                            bottom = 3.dp,
                                            start = 8.dp,
                                            end = 8.dp
                                        ),
                                    text = stringResource(id = R.string.business_period),
                                    style = Typography.body6,
                                    color = Color.White
                                )

                                Text(
                                    text = "${business.startDate.getShortenFormattedTime()}~${business.endDate.getShortenFormattedTime()}",
                                    style = Typography.body2
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    modifier = Modifier
                                        .background(
                                            color = Color.Transparent,
                                            shape = MaterialTheme.shapes.small
                                        )
                                        .border(width = 1.dp, color = Color(0xFFBECCE9))
                                        .padding(
                                            top = 3.dp,
                                            bottom = 3.dp,
                                            start = 8.dp,
                                            end = 8.dp
                                        ),
                                    text = stringResource(id = R.string.profit),
                                    style = Typography.body6,
                                    color = Main356DF8
                                )

                                Text(
                                    text = NumberFormat.getCurrencyInstance(Locale.KOREA)
                                        .format(business.revenue),
                                    style = Typography.body2
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(30.dp))

                        FRoundedArrowButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                viewModel.navigateTo(
                                    NavigateActions.DetailBusinessScreen
                                        .toBusinessMemberScreen(business.id)
                                )
                            },
                            content = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        modifier = Modifier.size(40.dp),
                                        painter = painterResource(id = R.drawable.ic_member_profile),
                                        tint = Color.Unspecified,
                                        contentDescription = null
                                    )

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Text(
                                        text = stringResource(id = R.string.business_members),
                                        style = Typography.body2
                                    )
                                }
                            },
                            number = business.memberEntities.size,
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        FRoundedArrowButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                viewModel.navigateTo(
                                    NavigateActions.DetailBusinessScreen
                                        .toBusinessTaskGraphScreen(business.id)
                                )
                            },
                            content = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        modifier = Modifier.size(40.dp),
                                        painter = painterResource(id = R.drawable.ic_graph),
                                        tint = Color.Unspecified,
                                        contentDescription = null
                                    )

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Text(
                                        text = stringResource(id = R.string.work_graph),
                                        style = Typography.body2
                                    )
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        FRoundedArrowButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                viewModel.navigateTo(
                                    NavigateActions.DetailBusinessScreen
                                        .toSummaryTaskScreen(business.id)
                                )
                            },
                            content = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        modifier = Modifier.size(40.dp),
                                        painter = painterResource(id = R.drawable.ic_calendar),
                                        tint = Color.Unspecified,
                                        contentDescription = null
                                    )

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Text(
                                        text = stringResource(id = R.string.task_by_day),
                                        style = Typography.body2
                                    )
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(30.dp))

                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(id = R.string.remark),
                            style = Typography.body3
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        FTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 130.dp, max = Dp.Infinity),
                            msgContent = business.description,
                            singleLine = false,
                            readOnly = true
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}
