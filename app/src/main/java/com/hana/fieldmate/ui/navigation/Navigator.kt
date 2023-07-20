package com.hana.fieldmate.ui.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface Navigator {
    val navActions: StateFlow<NavigateAction?>
    fun navigate(navAction: NavigateAction?)
}

class ComposeCustomNavigator : Navigator {
    private val _navActions: MutableStateFlow<NavigateAction?> by lazy {
        MutableStateFlow(null)
    }
    override val navActions: StateFlow<NavigateAction?>
        get() = _navActions.asStateFlow()

    override fun navigate(navAction: NavigateAction?) {
        _navActions.update { navAction }
    }
}