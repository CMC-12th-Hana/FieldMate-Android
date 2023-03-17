package com.hana.fieldmate.ui.task.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.repository.TaskRepository
import com.hana.fieldmate.domain.model.TaskEntity
import com.hana.fieldmate.domain.model.TaskMemberEntity
import com.hana.fieldmate.domain.toTaskEntityList
import com.hana.fieldmate.domain.toTaskMemberEntityList
import com.hana.fieldmate.network.TaskTypeQuery
import com.hana.fieldmate.network.di.NetworkLoadingState
import com.hana.fieldmate.ui.DialogAction
import com.hana.fieldmate.ui.DialogState
import com.hana.fieldmate.ui.Event
import com.hana.fieldmate.util.BAD_REQUEST_ERROR_MESSAGE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TaskListUiState(
    val taskList: List<TaskEntity> = listOf(),
    val taskMemberList: List<TaskMemberEntity> = listOf(),
    val taskListLoadingState: NetworkLoadingState = NetworkLoadingState.SUCCESS
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
        type: TaskTypeQuery
    ) {
        viewModelScope.launch {
            taskRepository.fetchTaskList(companyId, date, type)
                .onStart { _uiState.update { it.copy(taskListLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        result.data.let { taskListRes ->
                            if (type == TaskTypeQuery.TASK) {
                                _uiState.update {
                                    it.copy(
                                        taskList = taskListRes.taskList.toTaskEntityList(),
                                        taskListLoadingState = NetworkLoadingState.SUCCESS
                                    )
                                }
                            } else {
                                _uiState.update {
                                    it.copy(
                                        taskMemberList = taskListRes.memberTaskList.toTaskMemberEntityList(),
                                        taskListLoadingState = NetworkLoadingState.SUCCESS
                                    )
                                }
                            }
                        }
                    } else if (result is ResultWrapper.Error) {
                        _uiState.update {
                            it.copy(taskListLoadingState = NetworkLoadingState.FAILED)
                        }
                        if (result.errorMessage != BAD_REQUEST_ERROR_MESSAGE) {
                            sendEvent(
                                Event.Dialog(
                                    DialogState.JwtExpired,
                                    DialogAction.Open,
                                    result.errorMessage
                                )
                            )
                        } else {
                            sendEvent(
                                Event.Dialog(
                                    DialogState.Error,
                                    DialogAction.Open,
                                    result.errorMessage
                                )
                            )
                        }
                    }
                }
        }
    }
}