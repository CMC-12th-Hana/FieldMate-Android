package com.hana.fieldmate.ui.task

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.data.local.fakeTaskDataSource
import com.hana.fieldmate.domain.model.TaskEntity
import com.hana.fieldmate.getCurrentTime
import com.hana.fieldmate.ui.component.imagepicker.ImageInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TaskUiState(
    val taskEntity: TaskEntity = TaskEntity(0L, "", "", "", 0L, "", getCurrentTime(), "")
)

class TaskViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    private val _selectedImageList = mutableStateListOf<ImageInfo>()
    val selectedImageList = _selectedImageList

    init {
        val id: Long? = savedStateHandle["taskId"]
        if (id != null) loadTask(id)
    }

    fun loadTask(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(taskEntity = fakeTaskDataSource[id.toInt()]) }
        }

        selectImages(_uiState.value.taskEntity.images)
    }

    fun selectImages(selectedImages: List<ImageInfo>) {
        _selectedImageList.clear()
        _selectedImageList.addAll(selectedImages)
    }

    fun removeImage(image: ImageInfo) {
        _selectedImageList.remove(image)
    }
}