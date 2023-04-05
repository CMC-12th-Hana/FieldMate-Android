package com.hana.fieldmate.ui.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hana.fieldmate.App
import com.hana.fieldmate.FieldMateScreen
import com.hana.fieldmate.R
import com.hana.fieldmate.data.remote.model.response.JoinCompanyStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    fetchUserInfo: () -> Unit,
    navController: NavController
) {
    LaunchedEffect(true) {
        runBlocking { fetchUserInfo() }
        val userInfo = runBlocking { App.getInstance().getDataStore().getUserInfo().first() }

        delay(1000)

        // 로그인 상태이나 회사가 정해지지 않았다면 회사 선택 화면으로
        if (userInfo.isLoggedIn && userInfo.joinCompanyStatus == JoinCompanyStatus.PENDING) {
            navController.navigate(FieldMateScreen.SelectCompany.name) {
                popUpTo(FieldMateScreen.Splash.name) {
                    inclusive = true
                }
                launchSingleTop = true
            }
            // 로그인 상태면 홈 화면으로
        } else if (userInfo.isLoggedIn) {
            navController.navigate(FieldMateScreen.TaskGraph.name) {
                popUpTo(FieldMateScreen.Splash.name) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        } else {
            navController.navigate(FieldMateScreen.AuthGraph.name) {
                popUpTo(FieldMateScreen.Splash.name) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(120.dp, 206.dp),
            painter = painterResource(id = R.drawable.ic_splash_logo),
            tint = Color.Unspecified,
            contentDescription = null
        )
    }
}