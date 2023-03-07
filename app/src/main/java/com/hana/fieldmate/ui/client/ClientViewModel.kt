package com.hana.fieldmate.ui.client

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.model.request.SalesRepresentative
import com.hana.fieldmate.data.remote.model.request.UpdateClientReq
import com.hana.fieldmate.data.remote.repository.ClientRepository
import com.hana.fieldmate.data.toClientEntity
import com.hana.fieldmate.domain.model.ClientEntity
import com.hana.fieldmate.ui.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ClientUiState(
    val clientEntity: ClientEntity = ClientEntity(-1L, "", "", "", "", "", 0, 0)
)

@HiltViewModel
class ClientViewModel @Inject constructor(
    private val clientRepository: ClientRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(ClientUiState())
    val uiState: StateFlow<ClientUiState> = _uiState.asStateFlow()

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    val clientId: Long? = savedStateHandle["clientId"]

    fun sendEvent(event: Event) {
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }

    fun loadClient() {
        if (clientId != null) {
            viewModelScope.launch {
                clientRepository.fetchClientById(clientId)
                    .collect { result ->
                        if (result is ResultWrapper.Success) {
                            result.data.let { clientRes ->
                                _uiState.update {
                                    it.copy(clientEntity = clientRes.toClientEntity())
                                }
                            }
                        } else {
                            _uiState.update {
                                it.copy(clientEntity = ClientEntity())
                            }
                        }
                    }
            }
        }
    }

    fun createClient(
        name: String,
        tel: String,
        srName: String,
        srPhoneNumber: String,
        srDepartment: String
    ) {
        viewModelScope.launch {
            clientRepository.createClient(1L, name, tel, srName, srPhoneNumber, srDepartment)
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        sendEvent(Event.NavigateUp)
                    } else {
                        // TODO: 에러처리
                    }
                }
        }
    }

    fun updateClient(
        name: String,
        tel: String,
        srName: String,
        srPhoneNumber: String,
        srDepartment: String
    ) {
        viewModelScope.launch {
            clientRepository.updateClient(
                1L,
                UpdateClientReq(name, tel, SalesRepresentative(srName, srPhoneNumber, srDepartment))
            )
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        sendEvent(Event.NavigateUp)
                    } else {
                        // TODO: 에러 처리
                    }
                }
        }
    }
}