package com.hana.fieldmate.ui.business

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.repository.BusinessRepository
import com.hana.fieldmate.domain.model.BusinessEntity
import com.hana.fieldmate.domain.model.MemberEntity
import com.hana.fieldmate.network.di.NetworkLoadingState
import com.hana.fieldmate.ui.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class BusinessUiState(
    val businessEntity: BusinessEntity = BusinessEntity(
        0L,
        "",
        LocalDate.now(),
        LocalDate.now(),
        emptyList(),
        "",
        ""
    ),
    val businessLoadingState: NetworkLoadingState = NetworkLoadingState.LOADING
)

@HiltViewModel
class BusinessViewModel @Inject constructor(
    private val businessRepository: BusinessRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(BusinessUiState())
    val uiState: StateFlow<BusinessUiState> = _uiState.asStateFlow()

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    private val _selectedMemberListEntity = mutableStateListOf<MemberEntity>()
    val selectedMemberList = _selectedMemberListEntity

    val businessId: Long? = savedStateHandle["businessId"]

    fun sendEvent(event: Event) {
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }

    fun loadBusiness() {
        if (businessId != null) {
            viewModelScope.launch {
                businessRepository.fetchBusinessById(businessId)
                    .onStart { _uiState.update { it.copy(businessLoadingState = NetworkLoadingState.LOADING) } }
                    .collect { result ->
                        if (result is ResultWrapper.Success) {
                            result.data.let { businessRes ->
                                _uiState.update {
                                    it.copy(businessLoadingState = NetworkLoadingState.SUCCESS)
                                }
                            }
                        } else {
                            _uiState.update {
                                it.copy(businessLoadingState = NetworkLoadingState.FAILED)
                            }
                        }
                    }
            }
        }
    }

    fun createBusiness(
        clientId: Long,
        name: String,
        start: LocalDate,
        finish: LocalDate,
        memberIdList: List<Long>,
        revenue: Int,
        description: String
    ) {
        viewModelScope.launch {
            businessRepository.createBusiness(
                clientId,
                name,
                start,
                finish,
                memberIdList,
                revenue,
                description
            )
                .onStart { _uiState.update { it.copy(businessLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        result.data.let { businessCreateRes ->
                            _uiState.update {
                                it.copy(businessLoadingState = NetworkLoadingState.SUCCESS)
                            }
                        }
                    } else {
                        _uiState.update {
                            it.copy(businessLoadingState = NetworkLoadingState.FAILED)
                        }
                    }
                }
        }
    }

    fun updateBusiness(
        businessId: Long,
        name: String,
        start: LocalDate,
        finish: LocalDate,
        memberIdList: List<Long>,
        revenue: Int,
        description: String
    ) {
        viewModelScope.launch {
            businessRepository.updateBusiness(
                businessId,
                name,
                start,
                finish,
                memberIdList,
                revenue,
                description
            )
                .onStart { _uiState.update { it.copy(businessLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        result.data.let { businessUpdateRes ->
                            _uiState.update {
                                it.copy(businessLoadingState = NetworkLoadingState.SUCCESS)
                            }
                        }
                    } else {
                        _uiState.update {
                            it.copy(businessLoadingState = NetworkLoadingState.FAILED)
                        }
                    }
                }
        }
    }

    fun selectedMembers(selectedMemberEntities: List<MemberEntity>) {
        _selectedMemberListEntity.addAll(selectedMemberEntities)
    }

    fun removeMember(memberEntity: MemberEntity) {
        _selectedMemberListEntity.remove(memberEntity)
    }
}