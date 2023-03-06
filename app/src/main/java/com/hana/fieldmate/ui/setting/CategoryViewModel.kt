package com.hana.fieldmate.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.repository.TaskCategoryRepository
import com.hana.fieldmate.data.toCategoryEntityList
import com.hana.fieldmate.domain.model.CategoryEntity
import com.hana.fieldmate.network.di.NetworkLoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            categoryRepository.fetchTaskCategoryList(1L)
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
                    } else {
                        _uiState.update {
                            it.copy()
                        }
                    }
                }
        }
    }

    fun updateTaskCategory(
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
                    } else {
                        _uiState.update {
                            it.copy()
                        }
                    }
                }
        }
    }

    fun deleteTaskCategory(
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
                    } else {
                        _uiState.update {
                            it.copy()
                        }
                    }
                }
        }
    }
}