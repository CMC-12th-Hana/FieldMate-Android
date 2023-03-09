package com.hana.fieldmate.ui.member

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.R
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.repository.MemberRepository
import com.hana.fieldmate.data.toMemberEntity
import com.hana.fieldmate.domain.model.MemberEntity
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
    val memberEntity: MemberEntity = MemberEntity(
        0L,
        R.drawable.ic_member_profile,
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
    private val memberRepository: MemberRepository,
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
                memberRepository.fetchProfileById(memberId)
                    .onStart { _uiState.update { it.copy(memberLoadingState = NetworkLoadingState.LOADING) } }
                    .collect { result ->
                        if (result is ResultWrapper.Success) {
                            result.data.let { memberRes ->
                                _uiState.update {
                                    it.copy(
                                        memberEntity = memberRes.toMemberEntity(),
                                        memberLoadingState = NetworkLoadingState.SUCCESS
                                    )
                                }
                            }
                        } else {
                            _uiState.update {
                                it.copy(memberLoadingState = NetworkLoadingState.FAILED)
                            }
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
            memberRepository.createMember(companyId, name, phoneNumber, staffRank, staffNumber)
                .onStart { _uiState.update { it.copy(memberLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        sendEvent(Event.Dialog(DialogState.AddEdit, DialogAction.Open))
                    } else {
                        // TODO: 예외처리
                    }
                }
        }
    }

    fun updateProfile(
        name: String,
        staffNumber: String
    ) {
        viewModelScope.launch {
            memberRepository.updateProfile(name, staffNumber)
                .onStart { _uiState.update { it.copy(memberLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        sendEvent(Event.NavigateUp)
                    } else {
                        // TODO: 예외처리
                    }
                }
        }
    }
}