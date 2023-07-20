package com.hana.fieldmate.ui.setting.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.domain.model.CategoryEntity
import com.hana.fieldmate.domain.toCategoryEntityList
import com.hana.fieldmate.domain.usecase.CreateTaskCategoryUseCase
import com.hana.fieldmate.domain.usecase.DeleteTaskCategoryUseCase
import com.hana.fieldmate.domain.usecase.FetchTaskCategoryListUseCase
import com.hana.fieldmate.domain.usecase.UpdateTaskCategoryUseCase
import com.hana.fieldmate.network.di.NetworkLoadingState
import com.hana.fieldmate.ui.DialogEvent
import com.hana.fieldmate.ui.navigation.ComposeCustomNavigator
import com.hana.fieldmate.ui.navigation.NavigateAction
import com.hana.fieldmate.ui.navigation.NavigateActions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CategoryUiState(
    val categoryList: List<CategoryEntity> = emptyList(),
    val categoryListLoadingState: NetworkLoadingState = NetworkLoadingState.SUCCESS,
    val dialog: DialogEvent? = null
)

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val fetchTaskCategoryListUseCase: FetchTaskCategoryListUseCase,
    private val createTaskCategoryUseCase: CreateTaskCategoryUseCase,
    private val updateTaskCategoryUseCase: UpdateTaskCategoryUseCase,
    private val deleteTaskCategoryUseCase: DeleteTaskCategoryUseCase,
    private val navigator: ComposeCustomNavigator
) : ViewModel() {
    private val _uiState = MutableStateFlow(CategoryUiState())
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    fun loadCategories(companyId: Long) {
        viewModelScope.launch {
            fetchTaskCategoryListUseCase(companyId)
                .onStart { _uiState.update { it.copy(categoryListLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    when (result) {
                        is ResultWrapper.Success -> {
                            result.data.let { categoryListRes ->
                                _uiState.update {
                                    it.copy(
                                        categoryList = categoryListRes.toCategoryEntityList(),
                                        categoryListLoadingState = NetworkLoadingState.SUCCESS
                                    )
                                }
                            }
                        }
                        is ResultWrapper.Error -> {
                            _uiState.update {
                                it.copy(
                                    categoryListLoadingState = NetworkLoadingState.FAILED,
                                    dialog = DialogEvent.Error(result.error)
                                )
                            }
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
            createTaskCategoryUseCase(companyId, name, color)
                .onStart { _uiState.update { it.copy(categoryListLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    when (result) {
                        is ResultWrapper.Success -> {
                            loadCategories(companyId)
                        }
                        is ResultWrapper.Error -> {
                            _uiState.update {
                                it.copy(dialog = DialogEvent.Error(result.error))
                            }
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
            updateTaskCategoryUseCase(categoryId, name, color)
                .onStart { _uiState.update { it.copy(categoryListLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    when (result) {
                        is ResultWrapper.Success -> {
                            loadCategories(companyId)
                        }
                        is ResultWrapper.Error -> {
                            _uiState.update {
                                it.copy(dialog = DialogEvent.Error(result.error))
                            }
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
            deleteTaskCategoryUseCase(categoryList)
                .onStart { _uiState.update { it.copy(categoryListLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    when (result) {
                        is ResultWrapper.Success -> {
                            loadCategories(companyId)
                        }
                        is ResultWrapper.Error -> {
                            _uiState.update {
                                it.copy(dialog = DialogEvent.Error(result.error))
                            }
                        }
                    }
                }
        }
    }

    fun openAddEditDialog() {
        _uiState.update { it.copy(dialog = DialogEvent.AddEdit) }
    }

    fun openDeleteDialog() {
        _uiState.update { it.copy(dialog = DialogEvent.Delete) }
    }

    fun navigateTo(action: NavigateAction) {
        navigator.navigate(action)
    }

    fun backToLogin() {
        navigateTo(NavigateActions.backToLoginScreen())
    }

    fun onDialogClosed() {
        _uiState.update { it.copy(dialog = null) }
    }
}