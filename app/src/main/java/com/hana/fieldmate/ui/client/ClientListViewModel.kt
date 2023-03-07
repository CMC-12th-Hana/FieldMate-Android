package com.hana.fieldmate.ui.client

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.repository.ClientRepository
import com.hana.fieldmate.data.toClientEntityList
import com.hana.fieldmate.domain.model.ClientEntity
import com.hana.fieldmate.network.di.NetworkLoadingState
import com.hana.fieldmate.ui.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ClientListUiState(
    val clientEntityList: List<ClientEntity> = listOf(),
    val clientListLoadingState: NetworkLoadingState = NetworkLoadingState.FAILED
)

@HiltViewModel
class ClientListViewModel @Inject constructor(
    private val clientRepository: ClientRepository
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

    fun loadClients() {
        viewModelScope.launch {
            clientRepository.fetchClientList(1L)
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        result.data.let { clientListRes ->
                            _uiState.update {
                                it.copy(
                                    clientEntityList = clientListRes.toClientEntityList(),
                                    clientListLoadingState = NetworkLoadingState.SUCCESS
                                )
                            }
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                clientEntityList = emptyList(),
                                clientListLoadingState = NetworkLoadingState.FAILED
                            )
                        }
                    }
                }
        }
    }
}