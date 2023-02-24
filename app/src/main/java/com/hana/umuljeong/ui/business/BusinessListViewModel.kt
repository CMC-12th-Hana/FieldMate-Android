package com.hana.umuljeong.ui.business

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.umuljeong.data.datasource.fakeBusinessData
import com.hana.umuljeong.domain.Business
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BusinessListUiState(
    val businessList: List<Business> = listOf()
)

class BusinessListViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(BusinessListUiState())
    val uiState: StateFlow<BusinessListUiState> = _uiState.asStateFlow()

    init {
        loadBusinesses()
    }

    fun loadBusinesses() {
        viewModelScope.launch {
            _uiState.update { it.copy(businessList = fakeBusinessData) }
        }
    }
}