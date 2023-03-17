package com.hana.fieldmate.ui.setting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import com.hana.fieldmate.ui.component.FAppBarWithBackBtn

@Composable
fun WebViewScreen(
    modifier: Modifier = Modifier,
    title: String,
    contentUrl: String,
    navController: NavController
) {
    Scaffold(
        topBar = {
            FAppBarWithBackBtn(
                title = title,
                backBtnOnClick = {
                    navController.navigateUp()
                }
            )
        },
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val webViewState = rememberWebViewState(url = contentUrl)

            WebView(state = webViewState)
        }
    }
}