package com.hana.fieldmate.ui.task.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.model.OrderQuery
import com.hana.fieldmate.data.remote.model.SortQuery
import com.hana.fieldmate.domain.model.BusinessEntity
import com.hana.fieldmate.domain.model.CategoryEntity
import com.hana.fieldmate.domain.model.ClientEntity
import com.hana.fieldmate.domain.model.TaskEntity
import com.hana.fieldmate.domain.toBusinessEntityList
import com.hana.fieldmate.domain.toCategoryEntityList
import com.hana.fieldmate.domain.toClientEntityList
import com.hana.fieldmate.domain.toTaskEntity
import com.hana.fieldmate.domain.usecase.*
import com.hana.fieldmate.network.di.NetworkLoadingState
import com.hana.fieldmate.ui.DialogType
import com.hana.fieldmate.ui.component.imagepicker.ImageInfo
import com.hana.fieldmate.ui.navigation.ComposeCustomNavigator
import com.hana.fieldmate.ui.navigation.EditMode
import com.hana.fieldmate.ui.navigation.NavigateAction
import com.hana.fieldmate.ui.navigation.NavigateActions
import com.hana.fieldmate.ui.theme.CategoryColor
import com.hana.fieldmate.util.DateUtil.getCurrentTime
import dagger.hilt.android.lifecycle.HiltViewModel
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
        "",
        CategoryColor[0],
        getCurrentTime(),
        ""
    ),
    val taskLoadingState: NetworkLoadingState = NetworkLoadingState.SUCCESS,

    val clientList: List<ClientEntity> = emptyList(),
    val businessList: List<BusinessEntity> = emptyList(),
    val categoryList: List<CategoryEntity> = emptyList(),

    val mode: EditMode = EditMode.Add,
    val dialog: DialogType? = null
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
    private val navigator: ComposeCustomNavigator,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    // 업무 추가일 경우 선택한 이미지를 넘김
    private val _selectedImageList = mutableStateListOf<ImageInfo>()
    val selectedImageList = _selectedImageList

    // 업무 수정일 경우, 서버에서 불러온 이미지를 기준으로 추가할 이미지와 삭제할 이미지를 판별함
    // 추가할 이미지 파일과, 삭제할 이미지의 id 목록을 넘김
    private val loadedImageList = mutableListOf<ImageInfo>()
    private val addedImageList = mutableListOf<ImageInfo>()
    private val deletedImageList = mutableListOf<ImageInfo>()

    private val taskId: Long? = savedStateHandle["taskId"]

    init {
        _uiState.update {
            it.copy(mode = EditMode.valueOf(savedStateHandle["mode"] ?: "Add"))
        }

        loadTask()
    }

    fun loadTask() {
        if (taskId != null) {
            viewModelScope.launch {
                fetchTaskByIdUseCase(taskId)
                    .onStart { _uiState.update { it.copy(taskLoadingState = NetworkLoadingState.LOADING) } }
                    .collect { result ->
                        when (result) {
                            is ResultWrapper.Success -> {
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
                            }
                            is ResultWrapper.Error -> {
                                _uiState.update {
                                    it.copy(
                                        taskLoadingState = NetworkLoadingState.FAILED,
                                        dialog = DialogType.Error(result.error)
                                    )
                                }
                            }
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
                .collect { result ->
                    when (result) {
                        is ResultWrapper.Success -> {
                            result.data.let { clientListRes ->
                                _uiState.update {
                                    it.copy(clientList = clientListRes.toClientEntityList())
                                }
                            }
                        }
                        is ResultWrapper.Error -> {
                            _uiState.update { it.copy(clientList = emptyList()) }
                        }
                    }
                }
        }
    }

    fun loadBusinesses(clientId: Long) {
        viewModelScope.launch {
            fetchBusinessListByClientIdUseCase(clientId, null, null, null)
                .collect { result ->
                    when (result) {
                        is ResultWrapper.Success -> {
                            result.data.let { businessListRes ->
                                val now = LocalDate.now()
                                _uiState.update {
                                    it.copy(
                                        businessList = businessListRes.toBusinessEntityList()
                                            .filter { business ->
                                                now.isAfter(business.startDate.minusDays(1))
                                                        && now.isBefore(business.endDate.plusDays(1))
                                            }
                                    )
                                }
                            }
                        }
                        is ResultWrapper.Error -> {
                            _uiState.update { it.copy(businessList = emptyList()) }
                        }
                    }
                }
        }
    }

    fun loadCategories(companyId: Long) {
        viewModelScope.launch {
            fetchTaskCategoryListUseCase(companyId)
                .collect { result ->
                    when (result) {
                        is ResultWrapper.Success -> {
                            result.data.let { categoryListRes ->
                                _uiState.update {
                                    it.copy(
                                        categoryList = categoryListRes.toCategoryEntityList()
                                    )
                                }
                            }
                        }
                        is ResultWrapper.Error -> {
                            _uiState.update { it.copy(categoryList = emptyList()) }
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
            ).onStart { _uiState.update { it.copy(taskLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    when (result) {
                        is ResultWrapper.Success -> {
                            navigateTo(NavigateActions.navigateUp())
                        }
                        is ResultWrapper.Error -> {
                            _uiState.update { it.copy(dialog = DialogType.Error(result.error)) }
                        }
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
            )
                .onStart { _uiState.update { it.copy(taskLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    when (result) {
                        is ResultWrapper.Success -> {
                            navigateTo(NavigateActions.navigateUp())
                        }
                        is ResultWrapper.Error -> {
                            _uiState.update { it.copy(dialog = DialogType.Error(result.error)) }
                        }
                    }
                }
        }
    }

    fun deleteTask() {
        viewModelScope.launch {
            deleteTaskUseCase(taskId!!)
                .onStart { _uiState.update { it.copy(taskLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    when (result) {
                        is ResultWrapper.Success -> {
                            navigateTo(NavigateActions.navigateUp())
                        }
                        is ResultWrapper.Error -> {
                            _uiState.update { it.copy(dialog = DialogType.Error(result.error)) }
                        }
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

    fun openDetailImageDialog() {
        _uiState.update { it.copy(dialog = DialogType.Image) }
    }

    fun openPhotoPickerDialog() {
        _uiState.update { it.copy(dialog = DialogType.PhotoPick) }
    }

    fun openDeleteTaskDialog() {
        _uiState.update { it.copy(dialog = DialogType.Delete) }
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