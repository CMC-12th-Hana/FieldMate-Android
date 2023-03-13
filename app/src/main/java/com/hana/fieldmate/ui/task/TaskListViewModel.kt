package com.hana.fieldmate.ui.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.repository.TaskRepository
import com.hana.fieldmate.domain.model.TaskEntity
import com.hana.fieldmate.domain.model.TaskMemberEntity
import com.hana.fieldmate.domain.toTaskEntityList
import com.hana.fieldmate.domain.toTaskMemberEntityList
import com.hana.fieldmate.network.di.NetworkLoadingState
import com.hana.fieldmate.ui.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TaskListUiState(
    val taskEntityList: List<TaskEntity> = listOf(),
    val taskMemberEntityList: List<TaskMemberEntity> = listOf(),
    val taskListLoadingState: NetworkLoadingState = NetworkLoadingState.LOADING
)

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(TaskListUiState())
    val uiState: StateFlow<TaskListUiState> = _uiState.asStateFlow()

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    fun sendEvent(event: Event) {
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }

    fun loadTasks(
        companyId: Long,
        date: String,
        type: String
    ) {
        viewModelScope.launch {
            taskRepository.fetchTaskList(companyId, date, type)
                .onStart { _uiState.update { it.copy(taskListLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        result.data.let { taskListRes ->
                            if (type == "TASK") {
                                _uiState.update {
                                    it.copy(
                                        taskEntityList = taskListRes.taskList.toTaskEntityList(),
                                        taskListLoadingState = NetworkLoadingState.SUCCESS
                                    )
                                }
                            } else {
                                _uiState.update {
                                    it.copy(
                                        taskMemberEntityList = taskListRes.memberTaskList.toTaskMemberEntityList(),
                                        taskListLoadingState = NetworkLoadingState.SUCCESS
                                    )
                                }
                            }
                        }
                    } else {
                        _uiState.update {
                            it.copy(taskListLoadingState = NetworkLoadingState.FAILED)
                        }
                    }
                }
        }
    }
}