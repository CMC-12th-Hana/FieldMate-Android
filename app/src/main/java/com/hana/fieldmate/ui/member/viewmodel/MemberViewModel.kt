package com.hana.fieldmate.ui.member.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.R
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.domain.model.MemberEntity
import com.hana.fieldmate.domain.toMemberEntity
import com.hana.fieldmate.domain.usecase.*
import com.hana.fieldmate.network.di.NetworkLoadingState
import com.hana.fieldmate.ui.DialogAction
import com.hana.fieldmate.ui.DialogState
import com.hana.fieldmate.ui.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MemberUiState(
    val member: MemberEntity = MemberEntity(
        -1L,
        R.drawable.ic_member_profile,
        "",
        "",
        "",
        "",
        "",
        ""
    ),
    val memberLoadingState: NetworkLoadingState = NetworkLoadingState.LOADING
)

@HiltViewModel
class MemberViewModel @Inject constructor(
    private val fetchProfileByIdUseCase: FetchProfileByIdUseCase,
    private val createMemberUseCase: CreateMemberUseCase,
    private val updateMyProfileUseCase: UpdateMyProfileUseCase,
    private val updateMemberProfileUseCase: UpdateMemberProfileUseCase,
    private val deleteMemberUseCase: DeleteMemberUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(MemberUiState())
    val uiState: StateFlow<MemberUiState> = _uiState.asStateFlow()

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    val memberId: Long? = savedStateHandle["memberId"]

    fun sendEvent(event: Event) {
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }

    fun loadMember() {
        if (memberId != null) {
            viewModelScope.launch {
                fetchProfileByIdUseCase(memberId)
                    .onStart { _uiState.update { it.copy(memberLoadingState = NetworkLoadingState.LOADING) } }
                    .collect { result ->
                        if (result is ResultWrapper.Success) {
                            result.data.let { memberRes ->
                                _uiState.update {
                                    it.copy(
                                        member = memberRes.toMemberEntity(),
                                        memberLoadingState = NetworkLoadingState.SUCCESS
                                    )
                                }
                            }
                        } else if (result is ResultWrapper.Error) {
                            _uiState.update {
                                it.copy(memberLoadingState = NetworkLoadingState.FAILED)
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

    fun createMember(
        companyId: Long,
        name: String,
        phoneNumber: String,
        staffRank: String,
        staffNumber: String
    ) {
        viewModelScope.launch {
            createMemberUseCase(companyId, name, phoneNumber, staffRank, staffNumber)
                .onStart { _uiState.update { it.copy(memberLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        sendEvent(Event.Dialog(DialogState.AddEdit, DialogAction.Open))
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

    fun updateMyProfile(
        name: String,
        staffNumber: String,
        staffRank: String
    ) {
        viewModelScope.launch {
            updateMyProfileUseCase(name, staffNumber, staffRank)
                .onStart { _uiState.update { it.copy(memberLoadingState = NetworkLoadingState.LOADING) } }
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

    fun updateMemberProfile(
        name: String,
        phoneNumber: String,
        staffNumber: String,
        staffRank: String
    ) {
        viewModelScope.launch {
            updateMemberProfileUseCase(memberId!!, name, phoneNumber, staffNumber, staffRank)
                .onStart { _uiState.update { it.copy(memberLoadingState = NetworkLoadingState.LOADING) } }
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

    fun deleteMember() {
        viewModelScope.launch {
            deleteMemberUseCase(memberId!!)
                .onStart { _uiState.update { it.copy(memberLoadingState = NetworkLoadingState.LOADING) } }
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
}