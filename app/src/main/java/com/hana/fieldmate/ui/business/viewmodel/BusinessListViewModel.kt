package com.hana.fieldmate.ui.business.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.domain.model.BusinessEntity
import com.hana.fieldmate.domain.toBusinessEntityList
import com.hana.fieldmate.domain.usecase.FetchBusinessListUseCase
import com.hana.fieldmate.network.di.NetworkLoadingState
import com.hana.fieldmate.ui.DialogType
import com.hana.fieldmate.ui.navigation.ComposeCustomNavigator
import com.hana.fieldmate.ui.navigation.NavigateAction
import com.hana.fieldmate.ui.navigation.NavigateActions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BusinessListUiState(
    val businessList: List<BusinessEntity> = emptyList(),
    val businessListLoadingState: NetworkLoadingState = NetworkLoadingState.SUCCESS,
    val dialog: DialogType? = null
)

@HiltViewModel
class BusinessListViewModel @Inject constructor(
    private val fetchBusinessListUseCase: FetchBusinessListUseCase,
    private val navigator: ComposeCustomNavigator
) : ViewModel() {
    private val _uiState = MutableStateFlow(BusinessListUiState())
    val uiState: StateFlow<BusinessListUiState> = _uiState.asStateFlow()

    fun loadBusinesses(companyId: Long, name: String?, start: String?, finish: String?) {
        viewModelScope.launch {
            fetchBusinessListUseCase(companyId, name, start, finish)
                .onStart { _uiState.update { it.copy(businessListLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    when (result) {
                        is ResultWrapper.Success -> {
                            result.data.let { businessListRes ->
                                _uiState.update {
                                    it.copy(
                                        businessList = businessListRes.toBusinessEntityList(),
                                        businessListLoadingState = NetworkLoadingState.SUCCESS
                                    )
                                }
                            }
                        }
                        is ResultWrapper.Error -> {
                            _uiState.update {
                                it.copy(
                                    businessListLoadingState = NetworkLoadingState.FAILED,
                                    dialog = DialogType.Error(result.error)
                                )
                            }
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
}