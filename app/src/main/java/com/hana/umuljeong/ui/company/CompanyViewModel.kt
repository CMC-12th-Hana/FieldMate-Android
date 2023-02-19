package com.hana.umuljeong.ui.company

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

data class CompanyUiState(
    val company: Company = Company(0L, "", "", "", "", "", 0, 0)
)

class CompanyViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(CompanyUiState())
    val uiState: StateFlow<CompanyUiState> = _uiState.asStateFlow()

    init {
        val id: Long = savedStateHandle["companyId"]!!
        if (id != -1L) loadCompany(id)
    }

    fun loadCompany(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(company = fakeCompanyData[id.toInt()]) }
        }
    }
}