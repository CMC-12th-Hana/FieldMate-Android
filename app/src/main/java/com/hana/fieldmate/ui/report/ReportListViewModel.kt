package com.hana.fieldmate.ui.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.local.fakeReportDataSource
import com.hana.fieldmate.data.remote.repository.TaskRepository
import com.hana.fieldmate.domain.model.ReportEntity
import com.hana.fieldmate.network.di.NetworkLoadingState
import com.hana.fieldmate.ui.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ReportListUiState(
    val reportEntityList: List<ReportEntity> = listOf(),
    val reportListLoadingState: NetworkLoadingState = NetworkLoadingState.LOADING
)

@HiltViewModel
class ReportListViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ReportListUiState())
    val uiState: StateFlow<ReportListUiState> = _uiState.asStateFlow()

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    fun sendEvent(event: Event) {
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }

    fun loadReports(companyId: Long) {
        viewModelScope.launch {
            taskRepository.fetchTaskList(companyId)
                .onStart { _uiState.update { it.copy(reportListLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        result.data.let { taskListRes ->
                            _uiState.update {
                                it.copy(
                                    // TODO: 업무 리스트 불러오기
                                    reportListLoadingState = NetworkLoadingState.SUCCESS
                                )
                            }
                        }
                    } else {
                        _uiState.update {
                            it.copy(reportListLoadingState = NetworkLoadingState.FAILED)
                        }
                    }
                }

            _uiState.update { it.copy(reportEntityList = fakeReportDataSource) }
        }
    }
}