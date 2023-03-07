package com.hana.fieldmate.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.repository.TaskCategoryRepository
import com.hana.fieldmate.data.toCategoryEntityList
import com.hana.fieldmate.domain.model.CategoryEntity
import com.hana.fieldmate.network.di.NetworkLoadingState
import com.hana.fieldmate.ui.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CategoryUiState(
    val categoryEntityList: List<CategoryEntity> = emptyList(),
    val categoryListLoadingState: NetworkLoadingState = NetworkLoadingState.FAILED
)

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryRepository: TaskCategoryRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CategoryUiState())
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    fun sendEvent(event: Event) {
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }

    fun loadCategories(companyId: Long) {
        viewModelScope.launch {
            categoryRepository.fetchTaskCategoryList(companyId)
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        result.data.let { categoryListRes ->
                            _uiState.update {
                                it.copy(
                                    categoryEntityList = categoryListRes.toCategoryEntityList(),
                                    categoryListLoadingState = NetworkLoadingState.SUCCESS
                                )
                            }
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                categoryEntityList = emptyList(),
                                categoryListLoadingState = NetworkLoadingState.FAILED
                            )
                        }
                    }
                }
        }
    }

    fun createTaskCategory(
        companyId: Long,
        name: String,
        color: String
    ) {
        viewModelScope.launch {
            categoryRepository.createTaskCategory(companyId, name, color)
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        result.data.let { createTaskCategoryRes ->
                            _uiState.update {
                                it.copy()
                            }
                        }
                        loadCategories(companyId)
                    } else {
                        _uiState.update {
                            it.copy()
                        }
                    }
                }
        }
    }

    fun updateTaskCategory(
        companyId: Long,
        categoryId: Long,
        name: String,
        color: String
    ) {
        viewModelScope.launch {
            categoryRepository.updateTaskCategory(categoryId, name, color)
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        result.data.let { updateTaskCategoryRes ->
                            _uiState.update {
                                it.copy()
                            }
                        }
                        loadCategories(companyId)
                    } else {
                        _uiState.update {
                            it.copy()
                        }
                    }
                }
        }
    }

    fun deleteTaskCategory(
        companyId: Long,
        categoryList: List<Long>
    ) {
        viewModelScope.launch {
            categoryRepository.deleteTaskCategory(categoryList)
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        result.data.let { deleteTaskCategoryRes ->
                            _uiState.update {
                                it.copy()
                            }
                        }
                        loadCategories(companyId)
                    } else {
                        _uiState.update {
                            it.copy()
                        }
                    }
                }
        }
    }
}