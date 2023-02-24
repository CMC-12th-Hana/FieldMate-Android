package com.hana.umuljeong.ui.customer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.umuljeong.data.datasource.fakeCompanyData
import com.hana.umuljeong.data.model.Company
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CustomerUiState(
    val company: Company = Company(0L, "", "", "", "", "", 0, 0)
)

class CustomerViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(CustomerUiState())
    val uiState: StateFlow<CustomerUiState> = _uiState.asStateFlow()

    init {
        val id: Long = savedStateHandle["customerId"]!!
        if (id != -1L) loadCompany(id)
    }

    fun loadCompany(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(company = fakeCompanyData[id.toInt()]) }
        }
    }
}