package com.hana.fieldmate.ui.business.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.App
import com.hana.fieldmate.FieldMateScreen
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.domain.model.CategoryEntity
import com.hana.fieldmate.domain.model.TaskEntity
import com.hana.fieldmate.domain.toCategoryEntityList
import com.hana.fieldmate.domain.toLocalDateList
import com.hana.fieldmate.domain.toTaskEntityList
import com.hana.fieldmate.domain.usecase.FetchTaskCategoryListUseCase
import com.hana.fieldmate.domain.usecase.FetchTaskListByDateUseCase
import com.hana.fieldmate.network.di.NetworkLoadingState
import com.hana.fieldmate.ui.DialogAction
import com.hana.fieldmate.ui.DialogState
import com.hana.fieldmate.ui.Event
import com.hana.fieldmate.util.TOKEN_EXPIRED_MESSAGE
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
    val categoryListLoadingState: NetworkLoadingState = NetworkLoadingState.SUCCESS
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
                        if (result.errorMessage == TOKEN_EXPIRED_MESSAGE) {
                            sendEvent(
                                Event.Dialog(
                                    DialogState.JwtExpired,
                                    DialogAction.Open,
                                    result.errorMessage
                                )
                            )
                        } else {
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
                        if (result is ResultWrapper.Success) {
                            result.data.let { taskListRes ->
                                _uiState.update {
                                    it.copy(
                                        taskDateList = taskListRes.taskList.toLocalDateList(),
                                        taskDateListLoadingState = NetworkLoadingState.SUCCESS
                                    )
                                }
                            }
                        } else if (result is ResultWrapper.Error) {
                            _uiState.update {
                                it.copy(taskDateListLoadingState = NetworkLoadingState.FAILED)
                            }
                            if (result.errorMessage == TOKEN_EXPIRED_MESSAGE) {
                                App.getInstance().getDataStore().deleteAccessToken()
                                App.getInstance().getDataStore().deleteRefreshToken()
                                sendEvent(
                                    Event.NavigatePopUpTo(
                                        FieldMateScreen.Login.name,
                                        FieldMateScreen.TaskGraph.name,
                                        true
                                    )
                                )
                            } else {
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
                        if (result is ResultWrapper.Success) {
                            result.data.let { taskListRes ->
                                _uiState.update {
                                    it.copy(
                                        taskList = taskListRes.taskList.toTaskEntityList(),
                                        taskListLoadingState = NetworkLoadingState.SUCCESS
                                    )
                                }
                            }
                        } else if (result is ResultWrapper.Error) {
                            _uiState.update {
                                it.copy(taskListLoadingState = NetworkLoadingState.FAILED)
                            }
                            if (result.errorMessage == TOKEN_EXPIRED_MESSAGE) {
                                App.getInstance().getDataStore().deleteAccessToken()
                                App.getInstance().getDataStore().deleteRefreshToken()
                                sendEvent(
                                    Event.NavigatePopUpTo(
                                        FieldMateScreen.Login.name,
                                        FieldMateScreen.TaskGraph.name,
                                        true
                                    )
                                )
                            } else {
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
    }
}