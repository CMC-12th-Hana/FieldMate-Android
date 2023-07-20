package com.hana.fieldmate.ui.setting.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.App
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.domain.usecase.UpdateMyPasswordUseCase
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

data class ChangePasswordUiState(
    val passwordConditionList: List<Boolean> = listOf(false, false, false, false),
    val confirmPasswordCondition: Boolean = false,
    val dialog: DialogEvent? = null
)

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val updateMyPasswordUseCase: UpdateMyPasswordUseCase,
    private val navigator: ComposeCustomNavigator
) : ViewModel() {
    private val _uiState = MutableStateFlow(ChangePasswordUiState())
    val uiState: StateFlow<ChangePasswordUiState> = _uiState.asStateFlow()

    fun checkPassword(password: String) {
        val regExp = listOf(
            "^.{8,20}$",
            "^(?=.*[a-z])(?=.*[A-Z]).+",
            "^(?=.*[0-9]).+",
            """^(?=.*[-+_!@#\$%^&*., ?]).+"""
        )

        val conditions = mutableListOf(false, false, false, false)
        for (i: Int in conditions.indices) {
            conditions[i] = password.matches(regExp[i].toRegex())
        }

        _uiState.update { it.copy(passwordConditionList = conditions) }
    }

    fun checkConfirmPassword(oldPw: String, newPw: String) {
        val condition = oldPw == newPw
        _uiState.update { it.copy(confirmPasswordCondition = condition) }
    }

    fun checkConfirmEnabled(): Boolean {
        return _uiState.value.passwordConditionList.count { it } == 4 &&
                _uiState.value.confirmPasswordCondition
    }

    fun updateMyPassword(password: String, passwordCheck: String) {
        viewModelScope.launch {
            updateMyPasswordUseCase(password, passwordCheck)
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        runBlocking {
                            App.getInstance().getDataStore().deleteAccessToken()
                            App.getInstance().getDataStore().deleteRefreshToken()
                        }
                        _uiState.update {
                            it.copy(dialog = DialogEvent.Confirm)
                        }
                    } else if (result is ResultWrapper.Error) {
                        _uiState.update {
                            it.copy(dialog = DialogEvent.Error(result.error))
                        }
                    }
                }
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