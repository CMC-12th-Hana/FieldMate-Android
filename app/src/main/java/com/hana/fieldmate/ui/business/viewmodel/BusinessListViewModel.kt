package com.hana.fieldmate.ui.business.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.domain.model.BusinessEntity
import com.hana.fieldmate.domain.toBusinessEntityList
import com.hana.fieldmate.domain.usecase.FetchBusinessListUseCase
import com.hana.fieldmate.network.di.NetworkLoadingState
import com.hana.fieldmate.ui.DialogAction
import com.hana.fieldmate.ui.DialogState
import com.hana.fieldmate.ui.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BusinessListUiState(
    val businessList: List<BusinessEntity> = listOf(),
    val businessListLoadingState: NetworkLoadingState = NetworkLoadingState.LOADING
)

@HiltViewModel
class BusinessListViewModel @Inject constructor(
    private val fetchBusinessListUseCase: FetchBusinessListUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(BusinessListUiState())
    val uiState: StateFlow<BusinessListUiState> = _uiState.asStateFlow()

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    fun sendEvent(event: Event) {
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }

    fun loadBusinesses(
        companyId: Long,
        name: String? = null,
        start: String,
        finish: String
    ) {
        viewModelScope.launch {
            fetchBusinessListUseCase(companyId, name, start, finish)
                .onStart { _uiState.update { it.copy(businessListLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        result.data.let { businessListRes ->
                            _uiState.update {
                                it.copy(
                                    businessList = businessListRes.toBusinessEntityList(),
                                    businessListLoadingState = NetworkLoadingState.SUCCESS
                                )
                            }
                        }
                    } else if (result is ResultWrapper.Error) {
                        _uiState.update {
                            it.copy(
                                businessListLoadingState = NetworkLoadingState.FAILED
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