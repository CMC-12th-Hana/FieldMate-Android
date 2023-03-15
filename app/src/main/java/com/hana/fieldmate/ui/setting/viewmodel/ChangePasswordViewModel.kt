package com.hana.fieldmate.ui.setting.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.domain.usecase.UpdateMyPasswordUseCase
import com.hana.fieldmate.isValidString
import com.hana.fieldmate.ui.DialogAction
import com.hana.fieldmate.ui.DialogState
import com.hana.fieldmate.ui.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChangePasswordUiState(
    val passwordConditionList: List<Boolean> = listOf(false, false, false, false),
    val confirmPasswordCondition: Boolean = false
)

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val updateMyPasswordUseCase: UpdateMyPasswordUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ChangePasswordUiState())
    val uiState: StateFlow<ChangePasswordUiState> = _uiState.asStateFlow()

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    fun sendEvent(event: Event) {
        viewModelScope.launch {
            eventChannel.send(event)
        }
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

    fun checkConfirmEnabled(): Boolean {
        return _uiState.value.passwordConditionList.count { it } == 4 &&
                _uiState.value.confirmPasswordCondition
    }

    fun updateMyPassword(password: String, passwordCheck: String) {
        viewModelScope.launch {
            updateMyPasswordUseCase(password, passwordCheck)
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        sendEvent(Event.NavigateUp)
                    } else if (result is ResultWrapper.Error) {
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