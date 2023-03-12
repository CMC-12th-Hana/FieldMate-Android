package com.hana.fieldmate.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.hana.fieldmate.network.di.NetworkLoadingState

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
        else -> {
            content()
        }
    }
}