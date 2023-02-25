package com.hana.umuljeong.ui.client

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.umuljeong.data.datasource.fakeCompanyData
import com.hana.umuljeong.domain.Company
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ClientUiState(
    val company: Company = Company(0L, "", "", "", "", "", 0, 0)
)

class ClientViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(ClientUiState())
    val uiState: StateFlow<ClientUiState> = _uiState.asStateFlow()

    init {
        val id: Long? = savedStateHandle["clientId"]
        if (id != null) loadCompany(id)
    }

    fun loadCompany(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(company = fakeCompanyData[id.toInt()]) }
        }
    }
}