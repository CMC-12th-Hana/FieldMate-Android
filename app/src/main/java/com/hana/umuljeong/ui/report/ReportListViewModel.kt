package com.hana.umuljeong.ui.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.umuljeong.data.remote.datasource.fakeReportDataSource
import com.hana.umuljeong.domain.model.ReportEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ReportListUiState(
    val reportEntityList: List<ReportEntity> = listOf()
)

class ReportListViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ReportListUiState())
    val uiState: StateFlow<ReportListUiState> = _uiState.asStateFlow()

    init {
        loadReports()
    }

    fun loadReports() {
        viewModelScope.launch {
            _uiState.update { it.copy(reportEntityList = fakeReportDataSource) }
        }
    }
}