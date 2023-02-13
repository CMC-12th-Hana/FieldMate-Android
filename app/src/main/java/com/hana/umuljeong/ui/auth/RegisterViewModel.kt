package com.hana.umuljeong.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.umuljeong.isValidString
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class RegisterViewModel : ViewModel() {
    private val _registerDateState = MutableStateFlow(RegisterDataState())
    val registerDataState: StateFlow<RegisterDataState> = _registerDateState.asStateFlow()

    private var job: Job? = null

    fun checkName(name: String) {
        val condition = name.isNotEmpty()
        _registerDateState.update { it.copy(nameCondition = condition) }
    }

    fun checkEmail(email: String) {
        val condition = isValidString(email, "^[a-zA-Z0-9]+@[a-zA-Z0-9]+\$")
        _registerDateState.update { it.copy(emailCondition = condition) }
    }

    fun checkPhone(phone: String) {
        val condition = isValidString(phone, "\\d{10,11}")
        _registerDateState.update { it.copy(phoneCondition = condition) }
    }

    fun checkCertNumber() {
        _registerDateState.update { it.copy(certNumberCondition = true) }
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

        _registerDateState.update { it.copy(passwordConditionList = conditions) }
    }

    fun checkConfirmPassword(oldPw: String, newPw: String) {
        val condition = oldPw == newPw
        _registerDateState.update { it.copy(confirmPasswordCondition = condition) }
    }

    fun setTimer(seconds: Int) {
        _registerDateState.update { it.copy(remainSeconds = seconds) }

        job = decreaseSecond().onEach { sec ->
            _registerDateState.update { it.copy(remainSeconds = sec) }
        }.launchIn(viewModelScope)
    }

    private fun decreaseSecond(): Flow<Int> = flow {
        var i = _registerDateState.value.remainSeconds
        while (i > 0) {
            delay(1000)
            emit(--i)
        }
    }
}