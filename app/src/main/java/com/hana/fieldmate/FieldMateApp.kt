package com.hana.fieldmate

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.hana.fieldmate.ui.navigation.*
import kotlinx.coroutines.flow.Flow

@Composable
fun FieldMateApp(navigator: ComposeCustomNavigator) {
    val navController = rememberNavController()

    val lifecycleOwner = LocalLifecycleOwner.current
    val navigatorState by navigator.navActions.asLifecycleAwareState(
        lifecycleOwner = lifecycleOwner,
        initialState = null
    )
    LaunchedEffect(navigatorState) {
        navigatorState?.let {
            it.parcelableArguments.forEach { arg ->
                navController.currentBackStackEntry?.arguments?.putParcelable(arg.key, arg.value)
            }
            if (it.destination == NavigateActions.NavigateUp) {
                navController.navigateUp()
            } else {
                navController.navigate(it.destination, it.navOptions)
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = FieldMateScreen.SplashGraph.name
    ) {
        splashGraph(navController)
        authGraph(navController)
        taskGraph(navController)
        clientGraph(navController)
        businessGraph(navController)
        //memberGraph(navController)
        //settingGraph(navController)
    }
}

@Composable
fun <T> Flow<T>.asLifecycleAwareState(
    lifecycleOwner: LifecycleOwner,
    initialState: T
): State<T> {
    return lifecycleAwareState(
        lifecycleOwner = lifecycleOwner,
        flow = this,
        initialState = initialState
    )
}

@Composable
fun <T> lifecycleAwareState(
    lifecycleOwner: LifecycleOwner,
    flow: Flow<T>,
    initialState: T
): State<T> {
    val lifecycleAwareStateFlow = remember(flow, lifecycleOwner) {
        flow.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }

    return lifecycleAwareStateFlow.collectAsState(initialState)
}