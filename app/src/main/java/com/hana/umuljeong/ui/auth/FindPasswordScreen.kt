package com.hana.umuljeong.ui.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hana.umuljeong.R
import com.hana.umuljeong.ui.component.UAppBarWithBackBtn
import com.hana.umuljeong.ui.component.UButton
import com.hana.umuljeong.ui.component.UTextField
import com.hana.umuljeong.ui.component.UTextFieldWithTimer
import com.hana.umuljeong.ui.theme.*

@Composable
fun FindPasswordScreen(
    modifier: Modifier = Modifier,
    uiState: JoinUiState,
    checkPhone: (String) -> Unit,
    checkCertNumber: () -> Unit,
    setTimer: (Int) -> Unit,
    navController: NavController,
    confirmBtnOnClick: () -> Unit
) {
    var phone by rememberSaveable { mutableStateOf("") }
    var certNumber by rememberSaveable { mutableStateOf("") }

    var getCertNumber by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            UAppBarWithBackBtn(
                title = stringResource(id = R.string.find_password),
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
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 20.dp, end = 20.dp)
                    .weight(1f)
            ) {
                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = stringResource(id = R.string.find_password_info_first),
                    style = Typography.title1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(id = R.string.find_password_info_second),
                    style = Typography.body4
                )

                Spacer(modifier = Modifier.height(50.dp))

                Label(text = stringResource(id = R.string.phone))
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    UTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        msgContent = phone,
                        hint = stringResource(id = R.string.phone_hint),
                        isValid = uiState.phoneCondition,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone
                        ),
                        onValueChange = { phone = it }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    UButton(
                        onClick = {
                            getCertNumber = true
                            setTimer(180)
                        },
                        text = stringResource(id = R.string.receive_cert_number),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Transparent,
                            contentColor = Main356DF8,
                            disabledBackgroundColor = Color.Transparent,
                            disabledContentColor = BgD3D3D3
                        ),
                        enabled = uiState.phoneCondition,
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (uiState.phoneCondition) Main356DF8 else BgD3D3D3
                        ),
                        contentPadding = PaddingValues(all = 14.dp)
                    )
                }
                if (phone.isNotEmpty()) {
                    checkPhone(phone)

                    if (!uiState.phoneCondition) {
                        Spacer(modifier = Modifier.height(4.dp))
                        ConditionMessage(message = stringResource(id = R.string.check_phone_hint))
                    }
                }

                if (getCertNumber) {
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        UTextFieldWithTimer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            msgContent = certNumber,
                            remainSeconds = uiState.remainSeconds,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            onValueChange = { certNumber = it }
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        UButton(
                            onClick = { checkCertNumber() },
                            text = stringResource(id = R.string.confirm_cert_number),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Transparent,
                                contentColor = Main356DF8,
                                disabledBackgroundColor = Color.Transparent,
                                disabledContentColor = BgD3D3D3
                            ),
                            border = BorderStroke(
                                width = 1.dp,
                                color = Main356DF8
                            ),
                            contentPadding = PaddingValues(all = 14.dp)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
            ) {
                Spacer(Modifier.height(40.dp))

                UButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.confirm),
                    enabled = uiState.phoneCondition && uiState.certNumberCondition,
                    onClick = confirmBtnOnClick
                )

                Spacer(Modifier.height(50.dp))
            }
        }
    }
}