package com.hana.umuljeong.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.umuljeong.data.Result
import com.hana.umuljeong.data.repository.AuthRepository
import com.hana.umuljeong.isValidString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class JoinUiState(
    val nameCondition: Boolean = false,
    val phoneCondition: Boolean = false,
    val getCertNumber: Boolean = false,
    val remainSeconds: Int = 180,
    val timerRunning: Boolean = false,
    val certNumberCondition: Boolean = false,
    val passwordConditionList: List<Boolean> = listOf(false, false, false, false),
    val confirmPasswordCondition: Boolean = false,

    val joinState: JoinState = JoinState.FAILED
)

enum class JoinState {
    SUCCESS, FAILED
}

@HiltViewModel
class JoinViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(JoinUiState())
    val uiState: StateFlow<JoinUiState> = _uiState.asStateFlow()

    private var job: Job? = null

    fun join(name: String, phoneNumber: String, password: String, passwordCheck: String) {
        viewModelScope.launch {
            authRepository.join(name, phoneNumber, password, passwordCheck)
                .collect { result ->
                    if (result is Result.Success) {
                        result.data.let { joinRes ->
                            _uiState.update {
                                it.copy(
                                    joinState = JoinState.SUCCESS
                                )
                            }
                            authRepository.saveAccessToken(joinRes.accessToken)
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                joinState = JoinState.FAILED
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
        val condition = isValidString(phone, "\\d{10,11}")
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