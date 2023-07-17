package com.hana.fieldmate.ui.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.App
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.domain.usecase.CreateCompanyUseCase
import com.hana.fieldmate.domain.usecase.FetchUserInfoUseCase
import com.hana.fieldmate.domain.usecase.JoinCompanyUseCase
import com.hana.fieldmate.ui.DialogType
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

data class CompanyUiState(
    val dialog: DialogType? = null
)

@HiltViewModel
class CompanyViewModel @Inject constructor(
    private val fetchUserInfoUseCase: FetchUserInfoUseCase,
    private val createCompanyUseCase: CreateCompanyUseCase,
    private val joinCompanyUseCase: JoinCompanyUseCase,
    private val navigator: ComposeCustomNavigator
) : ViewModel() {
    private val _uiState = MutableStateFlow(CompanyUiState())
    val uiState: StateFlow<CompanyUiState> = _uiState.asStateFlow()

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

    fun createCompany(name: String) {
        viewModelScope.launch {
            createCompanyUseCase(name)
                .collect { result ->
                    when (result) {
                        is ResultWrapper.Success -> {
                            fetchUserInfo()
                            navigator.navigate(NavigateActions.AddCompanyScreen.toOnBoardingScreen())
                        }
                        is ResultWrapper.Error -> {
                            _uiState.update { it.copy(dialog = DialogType.Error(result.error)) }
                        }
                    }
                }
        }
    }

    fun joinCompany() {
        viewModelScope.launch {
            joinCompanyUseCase()
                .collect { result ->
                    when (result) {
                        is ResultWrapper.Success -> {
                            navigator.navigate(NavigateActions.AddCompanyScreen.toOnBoardingScreen())
                        }
                        is ResultWrapper.Error -> {
                            _uiState.update { it.copy(dialog = DialogType.Error(result.error)) }
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

    fun openSelectCompanyDialog() {
        _uiState.update { it.copy(dialog = DialogType.Select) }
    }
}