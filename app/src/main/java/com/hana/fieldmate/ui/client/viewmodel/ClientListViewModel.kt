package com.hana.fieldmate.ui.client.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.model.OrderQuery
import com.hana.fieldmate.data.remote.model.SortQuery
import com.hana.fieldmate.domain.model.ClientEntity
import com.hana.fieldmate.domain.toClientEntityList
import com.hana.fieldmate.domain.usecase.FetchClientListUseCase
import com.hana.fieldmate.network.di.NetworkLoadingState
import com.hana.fieldmate.ui.DialogEvent
import com.hana.fieldmate.ui.navigation.ComposeCustomNavigator
import com.hana.fieldmate.ui.navigation.NavigateAction
import com.hana.fieldmate.ui.navigation.NavigateActions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ClientListUiState(
    val clientList: List<ClientEntity> = emptyList(),
    val clientListLoadingState: NetworkLoadingState = NetworkLoadingState.SUCCESS,
    val dialog: DialogEvent? = null
)

@HiltViewModel
class ClientListViewModel @Inject constructor(
    private val fetchClientListUseCase: FetchClientListUseCase,
    private val navigator: ComposeCustomNavigator
) : ViewModel() {
    private val _uiState = MutableStateFlow(ClientListUiState())
    val uiState: StateFlow<ClientListUiState> = _uiState.asStateFlow()

    fun loadClients(companyId: Long, name: String?, sort: SortQuery?, order: OrderQuery?) {
        viewModelScope.launch {
            fetchClientListUseCase(companyId, name, sort, order)
                .onStart { _uiState.update { it.copy(clientListLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    when (result) {
                        is ResultWrapper.Success -> {
                            result.data.let { clientListRes ->
                                _uiState.update {
                                    it.copy(
                                        clientList = clientListRes.toClientEntityList(),
                                        clientListLoadingState = NetworkLoadingState.SUCCESS
                                    )
                                }
                            }
                        }
                        is ResultWrapper.Error -> {
                            _uiState.update {
                                it.copy(
                                    clientListLoadingState = NetworkLoadingState.FAILED,
                                    dialog = DialogEvent.Error(result.error)
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