package com.hana.umuljeong.ui.auth

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class RegisterViewModel : ViewModel() {
    private val _registerDateState = MutableStateFlow(RegisterDataState())
    val registerDataState: StateFlow<RegisterDataState> = _registerDateState

    fun checkName() {

    }

    fun checkEmail() {

    }

    fun checkPhone() {

    }

    fun checkCertNumber() {

    }

    fun checkPassword() {

    }

    fun checkConfirmPassword() {

    }

    fun checkRegisterBtn() {

    }
}