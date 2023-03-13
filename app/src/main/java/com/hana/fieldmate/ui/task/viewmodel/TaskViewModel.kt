package com.hana.fieldmate.ui.task.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.domain.model.BusinessEntity
import com.hana.fieldmate.domain.model.CategoryEntity
import com.hana.fieldmate.domain.model.ClientEntity
import com.hana.fieldmate.domain.model.TaskEntity
import com.hana.fieldmate.domain.toBusinessEntityList
import com.hana.fieldmate.domain.toCategoryEntityList
import com.hana.fieldmate.domain.toClientEntityList
import com.hana.fieldmate.domain.toTaskEntity
import com.hana.fieldmate.domain.usecase.*
import com.hana.fieldmate.getCurrentTime
import com.hana.fieldmate.network.OrderQuery
import com.hana.fieldmate.network.SortQuery
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
    val taskLoadingState: NetworkLoadingState = NetworkLoadingState.LOADING,

    val clientEntityList: List<ClientEntity> = listOf(),
    val clientListLoadingState: NetworkLoadingState = NetworkLoadingState.LOADING,

    val businessEntityList: List<BusinessEntity> = listOf(),
    val businessEntityListLoadingState: NetworkLoadingState = NetworkLoadingState.LOADING,

    val categoryEntityList: List<CategoryEntity> = emptyList(),
    val categoryEntityListLoadingState: NetworkLoadingState = NetworkLoadingState.LOADING
)

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

    private val taskId: Long? = savedStateHandle["taskId"]

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

    fun loadClients(
        companyId: Long,
        name: String? = null,
        sort: SortQuery? = null,
        order: OrderQuery? = null
    ) {
        viewModelScope.launch {
            fetchClientListUseCase(companyId, name, sort, order)
                .onStart { _uiState.update { it.copy(clientListLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        result.data.let { clientListRes ->
                            _uiState.update {
                                it.copy(
                                    clientEntityList = clientListRes.toClientEntityList(),
                                    clientListLoadingState = NetworkLoadingState.SUCCESS
                                )
                            }
                        }
                    } else if (result is ResultWrapper.Error) {
                        _uiState.update {
                            it.copy(
                                clientListLoadingState = NetworkLoadingState.FAILED
                            )
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

    fun loadBusinesses(clientId: Long) {
        viewModelScope.launch {
            fetchBusinessListByClientIdUseCase(clientId)
                .onStart { _uiState.update { it.copy(businessEntityListLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        result.data.let { businessListRes ->
                            _uiState.update {
                                it.copy(
                                    businessEntityList = businessListRes.toBusinessEntityList(),
                                    businessEntityListLoadingState = NetworkLoadingState.SUCCESS
                                )
                            }
                        }
                    } else if (result is ResultWrapper.Error) {
                        _uiState.update { it.copy(businessEntityListLoadingState = NetworkLoadingState.FAILED) }
                    }
                }
        }
    }

    fun loadCategories(companyId: Long) {
        viewModelScope.launch {
            fetchTaskCategoryListUseCase(companyId)
                .onStart { _uiState.update { it.copy(categoryEntityListLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        result.data.let { categoryListRes ->
                            _uiState.update {
                                it.copy(
                                    categoryEntityList = categoryListRes.toCategoryEntityList(),
                                    categoryEntityListLoadingState = NetworkLoadingState.SUCCESS
                                )
                            }
                        }
                    } else if (result is ResultWrapper.Error) {
                        _uiState.update {
                            it.copy(
                                categoryEntityListLoadingState = NetworkLoadingState.FAILED
                            )
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

    fun createTask(
        businessId: Long,
        taskCategoryId: Long,
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