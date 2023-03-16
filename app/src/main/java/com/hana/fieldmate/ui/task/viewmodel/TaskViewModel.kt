package com.hana.fieldmate.ui.task.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.FieldMateScreen
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
import com.hana.fieldmate.network.OrderQuery
import com.hana.fieldmate.network.SortQuery
import com.hana.fieldmate.network.di.NetworkLoadingState
import com.hana.fieldmate.ui.DialogAction
import com.hana.fieldmate.ui.DialogState
import com.hana.fieldmate.ui.Event
import com.hana.fieldmate.ui.component.imagepicker.ImageInfo
import com.hana.fieldmate.ui.theme.CategoryColor
import com.hana.fieldmate.util.BAD_REQUEST_ERROR_MESSAGE
import com.hana.fieldmate.util.DateUtil.getCurrentTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class TaskUiState(
    val task: TaskEntity = TaskEntity(
        -1L,
        -1L,
        -1L,
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
    val taskLoadingState: NetworkLoadingState = NetworkLoadingState.SUCCESS,

    val clientList: List<ClientEntity> = listOf(),
    val clientListLoadingState: NetworkLoadingState = NetworkLoadingState.LOADING,

    val businessList: List<BusinessEntity> = listOf(),
    val businessListLoadingState: NetworkLoadingState = NetworkLoadingState.LOADING,

    val categoryList: List<CategoryEntity> = emptyList(),
    val categoryListLoadingState: NetworkLoadingState = NetworkLoadingState.LOADING
)

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val fetchTaskByIdUseCase: FetchTaskByIdUseCase,
    private val createTaskUseCase: CreateTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
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

    // 업무 추가일 경우 선택한 이미지를 넘김
    private val _selectedImageList = mutableStateListOf<ImageInfo>()
    val selectedImageList = _selectedImageList

    // 업무 수정일 경우, 서버에서 불러온 이미지를 기준으로 추가할 이미지와 삭제할 이미지를 판별함
    // 추가할 이미지 파일과, 삭제할 이미지의 id 목록을 넘김
    private val loadedImageList = mutableListOf<ImageInfo>()
    private val addedImageList = mutableListOf<ImageInfo>()
    private val deletedImageList = mutableListOf<ImageInfo>()

    private val taskId: Long? = savedStateHandle["taskId"]

    var selectImageCall = 0

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
                                val task = taskRes.toTaskEntity()
                                _uiState.update {
                                    it.copy(
                                        task = taskRes.toTaskEntity(),
                                        taskLoadingState = NetworkLoadingState.SUCCESS
                                    )
                                }
                                loadImages(task.images)
                                selectImages(task.images)
                            }
                        } else if (result is ResultWrapper.Error) {
                            _uiState.update {
                                it.copy(taskLoadingState = NetworkLoadingState.FAILED)
                            }
                            if (result.errorMessage != BAD_REQUEST_ERROR_MESSAGE) {
                                sendEvent(
                                    Event.NavigatePopUpTo(
                                        destination = FieldMateScreen.Login.name,
                                        popUpDestination = FieldMateScreen.Login.name,
                                        inclusive = true,
                                        launchOnSingleTop = true
                                    )
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
                                    clientList = clientListRes.toClientEntityList(),
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
                    }
                }
        }
    }

    fun loadBusinesses(clientId: Long) {
        viewModelScope.launch {
            fetchBusinessListByClientIdUseCase(clientId, null, null, null)
                .onStart { _uiState.update { it.copy(businessListLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        result.data.let { businessListRes ->
                            val now = LocalDate.now()

                            _uiState.update {
                                it.copy(
                                    businessList = businessListRes.toBusinessEntityList()
                                        .filter { business ->
                                            now.isAfter(business.startDate.minusDays(1)) && now.isBefore(
                                                business.endDate.plusDays(1)
                                            )
                                        },
                                    businessListLoadingState = NetworkLoadingState.SUCCESS
                                )
                            }
                        }
                    } else if (result is ResultWrapper.Error) {
                        _uiState.update { it.copy(businessListLoadingState = NetworkLoadingState.FAILED) }
                    }
                }
        }
    }

    fun loadCategories(companyId: Long) {
        viewModelScope.launch {
            fetchTaskCategoryListUseCase(companyId)
                .onStart { _uiState.update { it.copy(categoryListLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        result.data.let { categoryListRes ->
                            _uiState.update {
                                it.copy(
                                    categoryList = categoryListRes.toCategoryEntityList(),
                                    categoryListLoadingState = NetworkLoadingState.SUCCESS
                                )
                            }
                        }
                    } else if (result is ResultWrapper.Error) {
                        _uiState.update {
                            it.copy(
                                categoryListLoadingState = NetworkLoadingState.FAILED
                            )
                        }
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
                    if (result.errorMessage != BAD_REQUEST_ERROR_MESSAGE) {
                        sendEvent(
                            Event.NavigatePopUpTo(
                                destination = FieldMateScreen.Login.name,
                                popUpDestination = FieldMateScreen.Login.name,
                                inclusive = true,
                                launchOnSingleTop = true
                            )
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

    fun updateTask(
        businessId: Long,
        taskCategoryId: Long,
        title: String,
        description: String
    ) {
        viewModelScope.launch {
            updateTaskUseCase(
                taskId!!,
                businessId,
                taskCategoryId,
                title,
                description,
                deletedImageList.map { it.id },
                addedImageList.map { it.contentUri }
            ).collect { result ->
                if (result is ResultWrapper.Success) {
                    sendEvent(Event.NavigateUp)
                } else if (result is ResultWrapper.Error) {
                    if (result.errorMessage != BAD_REQUEST_ERROR_MESSAGE) {
                        sendEvent(
                            Event.NavigatePopUpTo(
                                destination = FieldMateScreen.Login.name,
                                popUpDestination = FieldMateScreen.Login.name,
                                inclusive = true,
                                launchOnSingleTop = true
                            )
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

    fun deleteTask() {
        viewModelScope.launch {
            deleteTaskUseCase(taskId!!).collect { result ->
                if (result is ResultWrapper.Success) {
                    sendEvent(Event.NavigateUp)
                } else if (result is ResultWrapper.Error) {
                    if (result.errorMessage != BAD_REQUEST_ERROR_MESSAGE) {
                        sendEvent(
                            Event.NavigatePopUpTo(
                                destination = FieldMateScreen.Login.name,
                                popUpDestination = FieldMateScreen.Login.name,
                                inclusive = true,
                                launchOnSingleTop = true
                            )
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

    fun selectImages(images: List<ImageInfo>) {
        _selectedImageList.clear()
        _selectedImageList.addAll(images.filter { image ->
            !deletedImageList.map { it.id }.contains(image.id)
        })
        _selectedImageList.addAll(addedImageList)
    }

    fun unselectImage(image: ImageInfo) {
        _selectedImageList.remove(image)
    }

    private fun loadImages(images: List<ImageInfo>) {
        loadedImageList.clear()
        loadedImageList.addAll(images)
    }

    fun addImages(images: List<ImageInfo>) {
        addedImageList.addAll(images)
        _selectedImageList.addAll(images)
    }

    fun deleteImage(image: ImageInfo) {
        if (loadedImageList.contains(image)) {
            deletedImageList.add(image)
        }
        if (_selectedImageList.contains(image)) {
            _selectedImageList.remove(image)
        }
        if (addedImageList.contains(image)) {
            addedImageList.remove(image)
        }
    }
}