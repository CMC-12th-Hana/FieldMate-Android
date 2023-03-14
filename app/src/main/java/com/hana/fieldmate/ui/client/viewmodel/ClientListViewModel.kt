package com.hana.fieldmate.ui.client.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.domain.model.ClientEntity
import com.hana.fieldmate.domain.toClientEntityList
import com.hana.fieldmate.domain.usecase.FetchClientListUseCase
import com.hana.fieldmate.network.OrderQuery
import com.hana.fieldmate.network.SortQuery
import com.hana.fieldmate.network.di.NetworkLoadingState
import com.hana.fieldmate.ui.DialogAction
import com.hana.fieldmate.ui.DialogState
import com.hana.fieldmate.ui.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ClientListUiState(
    val clientList: List<ClientEntity> = listOf(),
    val clientListLoadingState: NetworkLoadingState = NetworkLoadingState.LOADING
)

@HiltViewModel
class ClientListViewModel @Inject constructor(
    private val fetchClientListUseCase: FetchClientListUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ClientListUiState())
    val uiState: StateFlow<ClientListUiState> = _uiState.asStateFlow()

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    fun sendEvent(event: Event) {
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }

    fun loadClients(companyId: Long, name: String?, sort: SortQuery?, order: OrderQuery?) {
        viewModelScope.launch {
            fetchClientListUseCase(companyId, name, sort, order)
                .onStart { _uiState.update { it.copy(clientListLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        result.data.let { clientListRes ->
                            _uiState.update {
                                it.copy(
                                    clientList = clientListRes.toClientEntityList(),
                                    clientListLoadingState = NetworkLoadingState.SUCCESS
                                )
                            }
                        }
                    } else if (result is ResultWrapper.Error) {
                        _uiState.update {
                            it.copy(
                                clientListLoadingState = NetworkLoadingState.FAILED
                            )
                        }
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