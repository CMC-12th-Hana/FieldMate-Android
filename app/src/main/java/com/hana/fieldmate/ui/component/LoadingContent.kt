package com.hana.fieldmate.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.hana.fieldmate.network.di.NetworkLoadingState
import com.hana.fieldmate.ui.theme.Typography
import com.hana.fieldmate.ui.theme.title1

@Composable
fun LoadingContent(
    loadingState: NetworkLoadingState,
    content: @Composable () -> Unit
) {
    when (loadingState) {
        NetworkLoadingState.LOADING -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        NetworkLoadingState.FAILED -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "데이터 로드 실패", style = Typography.title1)
            }
        }
        NetworkLoadingState.SUCCESS -> {
            content()
        }
    }
}