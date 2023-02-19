package com.hana.umuljeong.ui.company

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.umuljeong.data.datasource.fakeCompanyData
import com.hana.umuljeong.data.model.Company
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CompanyListUiState(
    val companyList: List<Company> = listOf()
)

class CompanyListViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CompanyListUiState())
    val uiState: StateFlow<CompanyListUiState> = _uiState.asStateFlow()

    init {
        loadCompanies()
    }

    fun loadCompanies() {
        viewModelScope.launch {
            _uiState.update { it.copy(companyList = fakeCompanyData) }
        }
    }
}