package com.hana.fieldmate.ui.setting.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.App
import com.hana.fieldmate.data.ErrorType
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.domain.usecase.QuitMemberUseCase
import com.hana.fieldmate.ui.DialogEvent
import com.hana.fieldmate.ui.navigation.ComposeCustomNavigator
import com.hana.fieldmate.ui.navigation.NavigateAction
import com.hana.fieldmate.ui.navigation.NavigateActions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

data class WithdrawlUiState(
    val dialog: DialogEvent? = null
)

@HiltViewModel
class WithdrawalViewModel @Inject constructor(
    private val quitMemberUseCase: QuitMemberUseCase,
    private val navigator: ComposeCustomNavigator
) : ViewModel() {
    private val _uiState = MutableStateFlow(WithdrawlUiState())
    val uiState: StateFlow<WithdrawlUiState> = _uiState.asStateFlow()

    fun quitMember() {
        viewModelScope.launch {
            quitMemberUseCase()
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        runBlocking {
                            App.getInstance().getDataStore().deleteAccessToken()
                            App.getInstance().getDataStore().deleteRefreshToken()
                        }
                        backToLogin()
                    } else if (result is ResultWrapper.Error) {
                        _uiState.update {
                            it.copy(dialog = DialogEvent.Error(result.error))
                        }
                    }
                }
        }
    }

    fun openConfirmAlertDialog() {
        _uiState.update {
            it.copy(
                dialog = DialogEvent.Error(
                    ErrorType.General("위 내용에 동의해주세요")
                )
            )
        }
    }

    fun navigateTo(action: NavigateAction) {
        navigator.navigate(action)
    }

    fun backToLogin() {
        navigateTo(NavigateActions.backToLoginScreen())
    }

    fun onDialogClosed() {
        _uiState.update { it.copy(dialog = null) }
    }
}