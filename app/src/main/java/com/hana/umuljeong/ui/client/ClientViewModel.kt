package com.hana.umuljeong.ui.client

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.umuljeong.data.remote.datasource.fakeClientDataSource
import com.hana.umuljeong.data.remote.repository.ClientRepository
import com.hana.umuljeong.domain.model.ClientEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ClientUiState(
    val clientEntity: ClientEntity = ClientEntity(0L, "", "", "", "", "", 0, 0)
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
            _uiState.update { it.copy(clientEntity = fakeClientDataSource[id.toInt()]) }
        }
    }
}