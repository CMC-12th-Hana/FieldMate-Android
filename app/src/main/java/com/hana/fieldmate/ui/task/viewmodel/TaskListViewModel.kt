package com.hana.fieldmate.ui.task.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.model.TaskTypeQuery
import com.hana.fieldmate.data.remote.repository.TaskRepository
import com.hana.fieldmate.domain.model.TaskEntity
import com.hana.fieldmate.domain.model.TaskMemberEntity
import com.hana.fieldmate.domain.toTaskEntityList
import com.hana.fieldmate.domain.toTaskMemberEntityList
import com.hana.fieldmate.network.di.NetworkLoadingState
import com.hana.fieldmate.ui.DialogEvent
import com.hana.fieldmate.ui.navigation.ComposeCustomNavigator
import com.hana.fieldmate.ui.navigation.NavigateAction
import com.hana.fieldmate.ui.navigation.NavigateActions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TaskListUiState(
    val taskList: List<TaskEntity> = emptyList(),
    val taskMemberList: List<TaskMemberEntity> = emptyList(),
    val taskListLoadingState: NetworkLoadingState = NetworkLoadingState.SUCCESS,
    val dialog: DialogEvent? = null
)

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val navigator: ComposeCustomNavigator
) : ViewModel() {
    private val _uiState = MutableStateFlow(TaskListUiState())
    val uiState: StateFlow<TaskListUiState> = _uiState.asStateFlow()

    fun loadTasks(
        companyId: Long,
        date: String,
        type: TaskTypeQuery
    ) {
        viewModelScope.launch {
            taskRepository.fetchTaskList(companyId, date, type)
                .onStart { _uiState.update { it.copy(taskListLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    when (result) {
                        is ResultWrapper.Success -> {
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
                        }
                        is ResultWrapper.Error -> {
                            _uiState.update {
                                it.copy(
                                    taskListLoadingState = NetworkLoadingState.FAILED,
                                    dialog = DialogEvent.Error(result.error)
                                )
                            }
                        }
                    }
                }
        }
    }

    fun backToLogin() {
        navigator.navigate(NavigateActions.backToLoginScreen())
    }

    fun navigateTo(action: NavigateAction) {
        navigator.navigate(action)
    }

    fun onDialogClosed() {
        _uiState.update { it.copy(dialog = null) }
    }
}