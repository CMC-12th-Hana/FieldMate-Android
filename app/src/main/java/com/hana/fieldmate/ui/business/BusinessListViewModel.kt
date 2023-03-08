package com.hana.fieldmate.ui.business

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.data.local.fakeBusinessDataSource
import com.hana.fieldmate.data.remote.repository.BusinessRepository
import com.hana.fieldmate.domain.model.BusinessEntity
import com.hana.fieldmate.network.di.NetworkLoadingState
import com.hana.fieldmate.ui.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BusinessListUiState(
    val businessEntityList: List<BusinessEntity> = listOf(),
    val businessListLoadingState: NetworkLoadingState = NetworkLoadingState.LOADING
)

@HiltViewModel
class BusinessListViewModel @Inject constructor(
    private val businessRepository: BusinessRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(BusinessListUiState())
    val uiState: StateFlow<BusinessListUiState> = _uiState.asStateFlow()

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    init {
        loadBusinesses()
    }

    fun loadBusinesses() {
        viewModelScope.launch {
            _uiState.update { it.copy(businessEntityList = fakeBusinessDataSource) }
        }
    }
}