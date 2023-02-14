package com.hana.umuljeong.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.hana.umuljeong.ui.component.UBottomBar

@Composable
fun BusinessScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Scaffold(
        bottomBar = {
            UBottomBar(
                navController = navController
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
        }
    }
}