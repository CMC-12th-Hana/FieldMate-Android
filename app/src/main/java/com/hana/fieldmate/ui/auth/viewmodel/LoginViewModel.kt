package com.hana.fieldmate.ui.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.App
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.domain.usecase.FetchUserInfoUseCase
import com.hana.fieldmate.domain.usecase.LoginUseCase
import com.hana.fieldmate.network.di.NetworkLoadingState
import com.hana.fieldmate.ui.DialogType
import com.hana.fieldmate.ui.navigation.ComposeCustomNavigator
import com.hana.fieldmate.ui.navigation.NavigateAction
import com.hana.fieldmate.ui.navigation.NavigateActions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

data class LoginUiState(
    val loginLoadingState: NetworkLoadingState = NetworkLoadingState.SUCCESS,
    val dialog: DialogType? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val fetchUserInfoUseCase: FetchUserInfoUseCase,
    private val navigator: ComposeCustomNavigator
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(phoneNumber: String, password: String) {
        viewModelScope.launch {
            loginUseCase(phoneNumber, password)
                .onStart { _uiState.update { it.copy(loginLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    when (result) {
                        is ResultWrapper.Success -> {
                            runBlocking {
                                App.getInstance().getDataStore()
                                    .saveAccessToken(result.data.accessToken)
                                App.getInstance().getDataStore()
                                    .saveRefreshToken(result.data.refreshToken)
                                fetchUserInfo()
                            }
                            navigator.navigate(NavigateActions.LoginScreen.toHomeScreen())
                            _uiState.update { it.copy(loginLoadingState = NetworkLoadingState.SUCCESS) }
                        }
                        is ResultWrapper.Error -> {
                            _uiState.update {
                                it.copy(
                                    loginLoadingState = NetworkLoadingState.FAILED,
                                    dialog = DialogType.Error(result.error)
                                )
                            }
                        }
                    }
                }
        }
    }

    fun fetchUserInfo() {
        runBlocking {
            fetchUserInfoUseCase()
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        result.data.let { user ->
                            App.getInstance().getDataStore().saveUserInfo(
                                user.companyId,
                                user.memberId,
                                user.companyName,
                                user.joinCompanyStatus,
                                user.name,
                                user.role
                            )
                            App.getInstance().updateUserInfo()
                        }
                    }
                }
        }
    }

    fun backToLogin() {
        navigator.navigate(NavigateActions.backToLoginScreen())
    }

    fun navigateTo(action: NavigateAction) {
        navigator.navigate(action)
    }

    fun onDialogClosed() {
        _uiState.update { it.copy(dialog = null) }
    }
}