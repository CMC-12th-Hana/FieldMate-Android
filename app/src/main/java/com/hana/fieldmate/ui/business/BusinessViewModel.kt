package com.hana.fieldmate.ui.business

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.data.*
import com.hana.fieldmate.data.remote.repository.BusinessRepository
import com.hana.fieldmate.data.remote.repository.MemberRepository
import com.hana.fieldmate.domain.model.BusinessEntity
import com.hana.fieldmate.domain.model.MemberNameEntity
import com.hana.fieldmate.network.di.NetworkLoadingState
import com.hana.fieldmate.ui.DialogAction
import com.hana.fieldmate.ui.DialogState
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
    val businessLoadingState: NetworkLoadingState = NetworkLoadingState.LOADING,
    val memberNameEntityList: List<MemberNameEntity> = listOf(),
    val memberNameListLoadingState: NetworkLoadingState = NetworkLoadingState.LOADING
)

@HiltViewModel
class BusinessViewModel @Inject constructor(
    private val businessRepository: BusinessRepository,
    private val memberRepository: MemberRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(BusinessUiState())
    val uiState: StateFlow<BusinessUiState> = _uiState.asStateFlow()

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    private val _selectedMemberListEntity = mutableStateListOf<MemberNameEntity>()
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
                                    it.copy(
                                        businessEntity = businessRes.toBusinessEntity(),
                                        businessLoadingState = NetworkLoadingState.SUCCESS
                                    )
                                }
                                selectMembers(businessRes.memberDtoList.toMemberNameEntityList())
                            }
                        } else if (result is ResultWrapper.Error) {
                            _uiState.update {
                                it.copy(businessLoadingState = NetworkLoadingState.FAILED)
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

    fun createBusiness(
        name: String,
        start: LocalDate,
        finish: LocalDate,
        revenue: Int,
        description: String
    ) {
        viewModelScope.launch {
            businessRepository.createBusiness(
                1L,
                name,
                start,
                finish,
                selectedMemberList.map { it.id },
                revenue,
                description
            )
                .onStart { _uiState.update { it.copy(businessLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        sendEvent(Event.NavigateUp)
                    } else if (result is ResultWrapper.Error) {
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

    fun updateBusiness(
        name: String,
        start: LocalDate,
        finish: LocalDate,
        revenue: Int,
        description: String
    ) {
        viewModelScope.launch {
            businessRepository.updateBusiness(
                businessId!!,
                name,
                start,
                finish,
                selectedMemberList.map { it.id },
                revenue,
                description
            )
                .onStart { _uiState.update { it.copy(businessLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        sendEvent(Event.NavigateUp)
                    } else if (result is ResultWrapper.Error) {
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

    fun loadMembers(companyId: Long) {
        viewModelScope.launch {
            memberRepository.fetchMemberList(companyId)
                .onStart { _uiState.update { it.copy(memberNameListLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        result.data.let { memberListRes ->
                            _uiState.update {
                                it.copy(
                                    memberNameEntityList = memberListRes.toMemberEntityList()
                                        .toMemberNameEntities(),
                                    memberNameListLoadingState = NetworkLoadingState.SUCCESS
                                )
                            }
                        }
                    } else if (result is ResultWrapper.Error) {
                        _uiState.update {
                            it.copy(
                                memberNameListLoadingState = NetworkLoadingState.FAILED
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

    fun updateBusinessMembers() {
        viewModelScope.launch {
            businessRepository.updateBusiness(
                businessId!!,
                _uiState.value.businessEntity.name,
                _uiState.value.businessEntity.startDate,
                _uiState.value.businessEntity.endDate,
                selectedMemberList.map { it.id },
                _uiState.value.businessEntity.revenue.toInt(),
                _uiState.value.businessEntity.description
            )
                .onStart { _uiState.update { it.copy(businessLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        sendEvent(Event.Dialog(DialogState.Select, DialogAction.Close))
                    } else if (result is ResultWrapper.Error) {
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

    fun selectMembers(memberNameList: List<MemberNameEntity>) {
        _selectedMemberListEntity.clear()
        _selectedMemberListEntity.addAll(memberNameList)
    }

    fun selectMember(memberNameEntity: MemberNameEntity) {
        _selectedMemberListEntity.add(memberNameEntity)
    }

    fun removeMember(memberNameEntity: MemberNameEntity) {
        _selectedMemberListEntity.remove(memberNameEntity)
    }
}