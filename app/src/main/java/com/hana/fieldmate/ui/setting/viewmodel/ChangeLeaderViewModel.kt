package com.hana.fieldmate.ui.setting.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.domain.toMemberEntityList
import com.hana.fieldmate.domain.usecase.FetchMemberListUseCase
import com.hana.fieldmate.domain.usecase.UpdateMemberToLeaderUseCase
import com.hana.fieldmate.network.di.NetworkLoadingState
import com.hana.fieldmate.ui.Event
import com.hana.fieldmate.ui.member.viewmodel.MemberListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeLeaderViewModel @Inject constructor(
    private val fetchMemberListUseCase: FetchMemberListUseCase,
    private val updateMemberToLeaderUseCase: UpdateMemberToLeaderUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(MemberListUiState())
    val uiState: StateFlow<MemberListUiState> = _uiState.asStateFlow()

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    fun sendEvent(event: Event) {
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }

    fun loadMembers(companyId: Long, name: String? = null) {
        viewModelScope.launch {
            fetchMemberListUseCase(companyId, name)
                .onStart { _uiState.update { it.copy(memberListLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        result.data.let { memberListRes ->
                            _uiState.update {
                                it.copy(
                                    memberList = memberListRes.toMemberEntityList(),
                                    memberListLoadingState = NetworkLoadingState.SUCCESS
                                )
                            }
                        }
                    } else if (result is ResultWrapper.Error) {
                        _uiState.update {
                            it.copy(
                                memberListLoadingState = NetworkLoadingState.FAILED,
                                error = result.error
                            )
                        }
                    }
                }
        }
    }

    fun updateMemberToLeader(memberId: Long) {
        viewModelScope.launch {
            updateMemberToLeaderUseCase(memberId)
                .onStart { _uiState.update { it.copy(memberListLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    when (result) {
                        is ResultWrapper.Success -> {
                            sendEvent(Event.NavigateUp)
                        }
                        is ResultWrapper.Error -> {
                            _uiState.update { it.copy(error = result.error) }
                        }
                    }
                }
        }
    }
}