package com.hana.fieldmate.ui.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.App
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.domain.usecase.CreateCompanyUseCase
import com.hana.fieldmate.domain.usecase.FetchUserInfoUseCase
import com.hana.fieldmate.domain.usecase.JoinCompanyUseCase
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

data class CompanyUiState(
    val companyLoadingState: NetworkLoadingState = NetworkLoadingState.SUCCESS,
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
                .onStart { _uiState.update { it.copy(companyLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    when (result) {
                        is ResultWrapper.Success -> {
                            fetchUserInfo()
                            navigateTo(NavigateActions.AddCompanyScreen.toOnBoardingScreen())
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
                .onStart { _uiState.update { it.copy(companyLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    when (result) {
                        is ResultWrapper.Success -> {
                            navigateTo(NavigateActions.AddCompanyScreen.toOnBoardingScreen())
                        }
                        is ResultWrapper.Error -> {
                            _uiState.update { it.copy(dialog = DialogType.Error(result.error)) }
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

    fun openSelectCompanyDialog() {
        _uiState.update { it.copy(dialog = DialogType.Select) }
    }
}