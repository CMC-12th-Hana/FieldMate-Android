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
import com.hana.fieldmate.network.di.NetworkLoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ClientUiState(
    val clientEntity: ClientEntity = ClientEntity(-1L, "", "", "", "", "", 0, 0),
    val clientLoadingState: NetworkLoadingState = NetworkLoadingState.FAILED
)

@HiltViewModel
class ClientViewModel @Inject constructor(
    private val clientRepository: ClientRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(ClientUiState())
    val uiState: StateFlow<ClientUiState> = _uiState.asStateFlow()

    init {
        val id: Long? = savedStateHandle["clientId"]
        if (id != null) loadClient(id)
    }

    fun loadClient(id: Long) {
        viewModelScope.launch {
            clientRepository.fetchClientById(id)
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        result.data.let { clientRes ->
                            _uiState.update {
                                it.copy(
                                    clientEntity = clientRes.toClientEntity(),
                                    clientLoadingState = NetworkLoadingState.SUCCESS
                                )
                            }
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                clientEntity = ClientEntity(),
                                clientLoadingState = NetworkLoadingState.FAILED
                            )
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
                        result.data.let { createClientRes ->
                            _uiState.update {
                                it.copy()
                            }
                        }
                    } else {
                        _uiState.update {
                            it.copy()
                        }
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
                        result.data.let { updateClientRes ->
                            _uiState.update {
                                it.copy()
                            }
                        }
                    } else {
                        _uiState.update {
                            it.copy()
                        }
                    }
                }
        }
    }
}