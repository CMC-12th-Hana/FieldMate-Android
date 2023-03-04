package com.hana.umuljeong.ui.client

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.umuljeong.data.remote.datasource.fakeClientDataSource
import com.hana.umuljeong.domain.model.ClientEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ClientListUiState(
    val clientEntityList: List<ClientEntity> = listOf()
)

class ClientListViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ClientListUiState())
    val uiState: StateFlow<ClientListUiState> = _uiState.asStateFlow()

    init {
        loadCompanies()
    }

    fun loadCompanies() {
        viewModelScope.launch {
            _uiState.update { it.copy(clientEntityList = fakeClientDataSource) }
        }
    }
}