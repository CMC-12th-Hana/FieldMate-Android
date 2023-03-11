package com.hana.fieldmate.ui.task

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.repository.TaskRepository
import com.hana.fieldmate.data.toTaskEntity
import com.hana.fieldmate.domain.model.TaskEntity
import com.hana.fieldmate.getCurrentTime
import com.hana.fieldmate.network.di.NetworkLoadingState
import com.hana.fieldmate.ui.DialogAction
import com.hana.fieldmate.ui.DialogState
import com.hana.fieldmate.ui.Event
import com.hana.fieldmate.ui.component.imagepicker.ImageInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TaskUiState(
    val taskEntity: TaskEntity = TaskEntity(0L, "", "", "", 0L, "", getCurrentTime(), ""),
    val taskLoadingState: NetworkLoadingState = NetworkLoadingState.LOADING
)

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
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
                taskRepository.fetchTaskById(taskId)
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
                    }
            }
        }
        selectImages(_uiState.value.taskEntity.images)
    }

    fun createTask(
        businessId: Long = 1L,
        taskCategoryId: Long = 3L,
        date: String,
        title: String,
        description: String
    ) {
        viewModelScope.launch {
            taskRepository.createTask(
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

    fun selectImages(selectedImages: List<ImageInfo>) {
        _selectedImageList.clear()
        _selectedImageList.addAll(selectedImages)
    }

    fun removeImage(image: ImageInfo) {
        _selectedImageList.remove(image)
    }
}