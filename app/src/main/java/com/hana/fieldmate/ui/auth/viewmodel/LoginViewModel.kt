package com.hana.fieldmate.ui.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.App
import com.hana.fieldmate.FieldMateScreen
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.domain.usecase.FetchUserInfoUseCase
import com.hana.fieldmate.domain.usecase.LoginUseCase
import com.hana.fieldmate.network.di.NetworkLoadingState
import com.hana.fieldmate.ui.DialogAction
import com.hana.fieldmate.ui.DialogState
import com.hana.fieldmate.ui.Event
import com.hana.fieldmate.util.TOKEN_EXPIRED_MESSAGE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

data class LoginUiState(
    val loginLoadingState: NetworkLoadingState = NetworkLoadingState.SUCCESS
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val fetchUserInfoUseCase: FetchUserInfoUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    fun sendEvent(event: Event) {
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }

    fun login(phoneNumber: String, password: String) {
        viewModelScope.launch {
            loginUseCase(phoneNumber, password)
                .onStart { _uiState.update { it.copy(loginLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        runBlocking {
                            App.getInstance().getDataStore()
                                .saveAccessToken(result.data.accessToken)
                            App.getInstance().getDataStore()
                                .saveRefreshToken(result.data.refreshToken)
                            fetchUserInfo()
                        }
                        sendEvent(
                            Event.NavigatePopUpTo(
                                FieldMateScreen.TaskGraph.name,
                                FieldMateScreen.AuthGraph.name,
                                inclusive = true,
                                launchOnSingleTop = true
                            )
                        )
                        _uiState.update {
                            it.copy(loginLoadingState = NetworkLoadingState.SUCCESS)
                        }
                    } else if (result is ResultWrapper.Error) {
                        _uiState.update {
                            it.copy(loginLoadingState = NetworkLoadingState.FAILED)
                        }
                        if (result.errorMessage == TOKEN_EXPIRED_MESSAGE) {
                            sendEvent(
                                Event.Dialog(
                                    DialogState.JwtExpired,
                                    DialogAction.Open,
                                    result.errorMessage
                                )
                            )
                        } else {
                            sendEvent(
                                Event.Dialog(
                                    DialogState.Error,
                                    DialogAction.Open,
                                    result.errorMessage
                                )
                            )
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
}