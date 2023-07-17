package com.hana.fieldmate.ui.business.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.data.ErrorType
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.domain.model.CategoryEntity
import com.hana.fieldmate.domain.model.TaskEntity
import com.hana.fieldmate.domain.toCategoryEntityList
import com.hana.fieldmate.domain.toLocalDateList
import com.hana.fieldmate.domain.toTaskEntityList
import com.hana.fieldmate.domain.usecase.FetchTaskCategoryListUseCase
import com.hana.fieldmate.domain.usecase.FetchTaskListByDateUseCase
import com.hana.fieldmate.network.di.NetworkLoadingState
import com.hana.fieldmate.ui.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class BusinessTaskUiState(
    val taskDateList: List<LocalDate> = emptyList(),
    val taskDateListLoadingState: NetworkLoadingState = NetworkLoadingState.SUCCESS,

    val taskList: List<TaskEntity> = emptyList(),
    val taskListLoadingState: NetworkLoadingState = NetworkLoadingState.SUCCESS,

    val categoryList: List<CategoryEntity> = mutableListOf(),
    val categoryListLoadingState: NetworkLoadingState = NetworkLoadingState.SUCCESS,

    val error: ErrorType? = null
)

@HiltViewModel
class BusinessTaskViewModel @Inject constructor(
    private val fetchTaskListByDateUseCase: FetchTaskListByDateUseCase,
    private val fetchTaskCategoryListUseCase: FetchTaskCategoryListUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(BusinessTaskUiState())
    val uiState: StateFlow<BusinessTaskUiState> = _uiState.asStateFlow()

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    val businessId: Long? = savedStateHandle["businessId"]

    fun sendEvent(event: Event) {
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }

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
                            _uiState.update { it.copy(error = result.error) }
                        }
                    }
                }
        }
    }

    fun loadTaskDateList(
        year: Int,
        month: Int,
        categoryId: Long?
    ) {
        if (businessId != null) {
            viewModelScope.launch {
                fetchTaskListByDateUseCase(
                    businessId = businessId,
                    year = year,
                    month = month,
                    categoryId = categoryId
                ).onStart { _uiState.update { it.copy(taskDateListLoadingState = NetworkLoadingState.LOADING) } }
                    .collect { result ->
                        when (result) {
                            is ResultWrapper.Success -> {
                                result.data.let { taskListRes ->
                                    _uiState.update {
                                        it.copy(
                                            taskDateList = taskListRes.taskList.toLocalDateList(),
                                            taskDateListLoadingState = NetworkLoadingState.SUCCESS
                                        )
                                    }
                                }
                            }
                            is ResultWrapper.Error -> {
                                _uiState.update { it.copy(error = result.error) }
                            }
                        }
                    }
            }
        }
    }

    fun loadTaskListByDate(
        year: Int,
        month: Int,
        day: Int? = null,
        categoryId: Long? = null
    ) {
        if (businessId != null) {
            viewModelScope.launch {
                fetchTaskListByDateUseCase(businessId, year, month, day, categoryId)
                    .onStart { _uiState.update { it.copy(taskListLoadingState = NetworkLoadingState.LOADING) } }
                    .collect { result ->
                        when (result) {
                            is ResultWrapper.Success -> {
                                result.data.let { taskListRes ->
                                    _uiState.update {
                                        it.copy(
                                            taskList = taskListRes.taskList.toTaskEntityList(),
                                            taskListLoadingState = NetworkLoadingState.SUCCESS
                                        )
                                    }
                                }
                            }
                            is ResultWrapper.Error -> {
                                _uiState.update { it.copy(error = result.error) }
                            }
                        }
                    }
            }
        }
    }
}