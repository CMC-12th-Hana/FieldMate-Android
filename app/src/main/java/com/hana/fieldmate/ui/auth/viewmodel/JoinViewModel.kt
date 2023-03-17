package com.hana.fieldmate.ui.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.App
import com.hana.fieldmate.FieldMateScreen
import com.hana.fieldmate.StringUtil.isValidString
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.model.request.MessageType
import com.hana.fieldmate.domain.usecase.JoinUseCase
import com.hana.fieldmate.domain.usecase.SendMessageUseCase
import com.hana.fieldmate.domain.usecase.VerifyMessageUseCase
import com.hana.fieldmate.ui.DialogAction
import com.hana.fieldmate.ui.DialogState
import com.hana.fieldmate.ui.Event
import com.hana.fieldmate.util.TOKEN_EXPIRED_MESSAGE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

data class JoinUiState(
    val nameCondition: Boolean = false,
    val phoneCondition: Boolean = false,
    val getCertNumber: Boolean = false,
    val remainSeconds: Int = 180,
    val timerRunning: Boolean = false,
    val certNumberCondition: Boolean = false,
    val passwordConditionList: List<Boolean> = listOf(false, false, false, false),
    val confirmPasswordCondition: Boolean = false
)

@HiltViewModel
class JoinViewModel @Inject constructor(
    private val joinUseCase: JoinUseCase,
    private val verifyMessageUseCase: VerifyMessageUseCase,
    private val sendMessageUseCase: SendMessageUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(JoinUiState())
    val uiState: StateFlow<JoinUiState> = _uiState.asStateFlow()

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    private var job: Job? = null

    fun sendEvent(event: Event) {
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }

    fun join(name: String, phoneNumber: String, password: String, passwordCheck: String) {
        viewModelScope.launch {
            joinUseCase(name, phoneNumber, password, passwordCheck)
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        result.data.let { joinRes ->
                            runBlocking {
                                App.getInstance().getDataStore()
                                    .saveAccessToken(joinRes.accessToken)
                                App.getInstance().getDataStore()
                                    .saveRefreshToken(joinRes.refreshToken)
                            }
                        }
                        sendEvent(Event.NavigateTo(FieldMateScreen.SelectCompany.name))
                    } else if (result is ResultWrapper.Error) {
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

    fun verifyMessage(phoneNumber: String, authenticationNumber: String, messageType: MessageType) {
        viewModelScope.launch {
            verifyMessageUseCase(phoneNumber, authenticationNumber, messageType)
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        _uiState.update {
                            it.copy(certNumberCondition = true)
                        }
                        sendEvent(Event.Dialog(DialogState.Confirm, DialogAction.Open))
                    } else if (result is ResultWrapper.Error) {
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

    fun sendMessage(phoneNumber: String, messageType: MessageType) {
        viewModelScope.launch {
            sendMessageUseCase(phoneNumber, messageType)
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        setTimer(180)
                    } else if (result is ResultWrapper.Error) {
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

    fun checkName(name: String) {
        val condition = name.isNotEmpty()
        _uiState.update { it.copy(nameCondition = condition) }
    }

    fun checkPhone(phone: String) {
        val condition = isValidString(phone, """^01([016789])-?([0-9]{3,4})-?([0-9]{4})$""")
        _uiState.update { it.copy(phoneCondition = condition) }
    }

    fun checkCertNumber() {
        _uiState.update { it.copy(certNumberCondition = true) }
    }

    fun checkPassword(password: String) {
        val regExp = listOf(
            "^.{8,20}$",
            "^(?=.*[a-z])(?=.*[A-Z]).+",
            "^(?=.*[0-9]).+",
            """^(?=.*[-+_!@#\$%^&*., ?]).+"""
        )

        val conditions = mutableListOf(false, false, false, false)
        for (i: Int in conditions.indices) {
            conditions[i] = isValidString(password, regExp[i])
        }

        _uiState.update { it.copy(passwordConditionList = conditions) }
    }

    fun checkConfirmPassword(oldPw: String, newPw: String) {
        val condition = oldPw == newPw
        _uiState.update { it.copy(confirmPasswordCondition = condition) }
    }

    fun checkRegisterEnabled(): Boolean {
        return _uiState.value.nameCondition &&
                _uiState.value.phoneCondition &&
                _uiState.value.certNumberCondition &&
                _uiState.value.passwordConditionList.count { it } == 4 &&
                _uiState.value.confirmPasswordCondition
    }

    fun setTimer(seconds: Int) {
        _uiState.update { it.copy(timerRunning = true, remainSeconds = seconds) }

        job = decreaseSecond().onEach { sec ->
            _uiState.update { it.copy(remainSeconds = sec) }
        }.launchIn(viewModelScope)
    }

    // 타이머 시간이 0초가 될 시 확인 버튼을 눌러 타이머를 종료
    fun checkTimer() {
        _uiState.update { it.copy(timerRunning = false) }
    }

    private fun decreaseSecond(): Flow<Int> = flow {
        var i = _uiState.value.remainSeconds
        while (i > 0) {
            delay(1000)
            emit(--i)
        }
    }
}