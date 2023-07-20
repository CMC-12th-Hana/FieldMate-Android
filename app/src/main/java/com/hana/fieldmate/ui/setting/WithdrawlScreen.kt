package com.hana.fieldmate.ui.setting

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hana.fieldmate.R
import com.hana.fieldmate.data.ErrorType
import com.hana.fieldmate.ui.DialogEvent
import com.hana.fieldmate.ui.component.BackToLoginDialog
import com.hana.fieldmate.ui.component.ErrorDialog
import com.hana.fieldmate.ui.component.FAppBarWithBackBtn
import com.hana.fieldmate.ui.component.FButton
import com.hana.fieldmate.ui.navigation.NavigateActions
import com.hana.fieldmate.ui.setting.viewmodel.WithdrawalViewModel
import com.hana.fieldmate.ui.theme.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WithdrawalScreen(
    modifier: Modifier = Modifier,
    viewModel: WithdrawalViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isChecked by remember { mutableStateOf(false) }

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

    Scaffold(
        topBar = {
            FAppBarWithBackBtn(
                title = stringResource(id = R.string.setting),
                backBtnOnClick = {
                    viewModel.navigateTo(NavigateActions.navigateUp())
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(start = 20.dp, end = 20.dp),
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            Text(text = stringResource(id = R.string.terms_of_use), style = Typography.title2)
            Spacer(modifier = Modifier.height(20.dp))

            Column {
                Row {
                    Text(text = "지금까지 ", style = Typography.body3)
                    Text(text = "Field Mate ", style = Typography.body3, color = Main356DF8)
                    Text(text = "서비스를 이용해주셔서 감사합니다.", style = Typography.body3)
                }
                Text(
                    text = "회원을 탈퇴하면 서비스 내 나의 계정 정보 및 업무 히스토리 내역이 삭제되고 복구 할 수 없습니다.",
                    style = Typography.body3
                )
            }

            Spacer(modifier = Modifier.height(60.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { isChecked = !isChecked },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Main356DF8,
                            uncheckedColor = BgF1F1F5,
                            checkmarkColor = Color.White
                        )
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    modifier = Modifier.offset(y = (-1).dp),
                    text = stringResource(id = R.string.confirm_check_message),
                    style = Typography.body3
                )
            }
            Spacer(modifier = Modifier.height(30.dp))

            FButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.quit),
                onClick = {
                    if (!isChecked) {
                        viewModel.openConfirmAlertDialog()
                    } else {
                        viewModel.quitMember()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Font70747E,
                    contentColor = Color.White
                ),
                border = BorderStroke(width = 1.dp, color = Color.White)
            )
        }
    }
}