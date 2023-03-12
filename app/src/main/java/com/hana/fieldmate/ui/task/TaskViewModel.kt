package com.hana.fieldmate.ui.task

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.toTaskEntity
import com.hana.fieldmate.domain.model.TaskEntity
import com.hana.fieldmate.domain.usecase.*
import com.hana.fieldmate.getCurrentTime
import com.hana.fieldmate.network.di.NetworkLoadingState
import com.hana.fieldmate.ui.DialogAction
import com.hana.fieldmate.ui.DialogState
import com.hana.fieldmate.ui.Event
import com.hana.fieldmate.ui.component.imagepicker.ImageInfo
import com.hana.fieldmate.ui.theme.CategoryColor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TaskUiState(
    val taskEntity: TaskEntity = TaskEntity(
        -1L,
        -1L,
        "",
        "",
        "",
        "",
        CategoryColor[0],
        getCurrentTime(),
        ""
    ),
    val taskLoadingState: NetworkLoadingState = NetworkLoadingState.LOADING
)

// TODO: 고객사, 사업, 카테고리 드롭다운으로 선택해서 id 넘기기
@HiltViewModel
class TaskViewModel @Inject constructor(
    private val fetchTaskByIdUseCase: FetchTaskByIdUseCase,
    private val createTaskUseCase: CreateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val fetchClientListUseCase: FetchClientListUseCase,
    private val fetchBusinessListByClientIdUseCase: FetchBusinessListByClientIdUseCase,
    private val fetchTaskCategoryListUseCase: FetchTaskCategoryListUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    private val _selectedImageList = mutableStateListOf<ImageInfo>()
    val selectedImageList = _selectedImageList

    val taskId: Long? = savedStateHandle["taskId"]

    fun sendEvent(event: Event) {
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }

    fun loadTask() {
        if (taskId != null) {
            viewModelScope.launch {
                fetchTaskByIdUseCase(taskId)
                    .onStart { _uiState.update { it.copy(taskLoadingState = NetworkLoadingState.LOADING) } }
                    .collect { result ->
                        if (result is ResultWrapper.Success) {
                            result.data.let { taskRes ->
                                _uiState.update {
                                    it.copy(
                                        taskEntity = taskRes.toTaskEntity(),
                                        taskLoadingState = NetworkLoadingState.SUCCESS
                                    )
                                }
                            }
                        } else if (result is ResultWrapper.Error) {
                            _uiState.update {
                                it.copy(taskLoadingState = NetworkLoadingState.FAILED)
                            }
                            sendEvent(
                                Event.Dialog(
                                    DialogState.Error,
                                    DialogAction.Open,
                                    result.errorMessage
                                )
                            )
                        }
                        selectImages(_uiState.value.taskEntity.images)
                    }
            }
        }
    }

    fun createTask(
        businessId: Long = 1L,
        taskCategoryId: Long = 3L,
        date: String,
        title: String,
        description: String
    ) {
        viewModelScope.launch {
            createTaskUseCase(
                businessId,
                taskCategoryId,
                date,
                title,
                description,
                selectedImageList.map { it.contentUri }
            ).collect { result ->
                if (result is ResultWrapper.Success) {
                    sendEvent(Event.NavigateUp)
                } else if (result is ResultWrapper.Error) {
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

    fun deleteTask() {
        viewModelScope.launch {
            deleteTaskUseCase(taskId!!).collect { result ->
                if (result is ResultWrapper.Success) {
                    sendEvent(Event.NavigateUp)
                } else if (result is ResultWrapper.Error) {
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

    fun selectImages(selectedImages: List<ImageInfo>) {
        _selectedImageList.addAll(selectedImages)
    }

    fun removeImage(image: ImageInfo) {
        _selectedImageList.remove(image)
    }
}