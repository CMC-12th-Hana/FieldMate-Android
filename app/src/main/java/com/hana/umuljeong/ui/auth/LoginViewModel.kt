package com.hana.umuljeong.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.umuljeong.data.ResultWrapper
import com.hana.umuljeong.data.remote.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val loginState: LoginState = LoginState.FAILED
)

enum class LoginState {
    SUCCESS, FAILED
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(phoneNumber: String, password: String) {
        viewModelScope.launch {
            authRepository.login(phoneNumber, password)
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        _uiState.update {
                            it.copy(
                                loginState = LoginState.SUCCESS
                            )
                        }
                        authRepository.saveAccessToken(result.data.accessToken)
                    } else {
                        _uiState.update {
                            it.copy(
                                loginState = LoginState.FAILED
                            )
                        }
                    }
                }
        }
    }
}