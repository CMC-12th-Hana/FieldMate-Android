package com.hana.fieldmate.ui.member.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.data.ErrorType
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.domain.model.MemberEntity
import com.hana.fieldmate.domain.toMemberEntityList
import com.hana.fieldmate.domain.usecase.FetchMemberListUseCase
import com.hana.fieldmate.network.di.NetworkLoadingState
import com.hana.fieldmate.ui.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MemberListUiState(
    val memberList: List<MemberEntity> = emptyList(),
    val memberListLoadingState: NetworkLoadingState = NetworkLoadingState.SUCCESS,
    val error: ErrorType? = null
)

@HiltViewModel
class MemberListViewModel @Inject constructor(
    private val fetchMemberListUseCase: FetchMemberListUseCase
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
                    when (result) {
                        is ResultWrapper.Success -> {
                            result.data.let { memberListRes ->
                                _uiState.update {
                                    it.copy(
                                        memberList = memberListRes.toMemberEntityList(),
                                        memberListLoadingState = NetworkLoadingState.SUCCESS
                                    )
                                }
                            }
                        }
                        is ResultWrapper.Error -> {
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
    }
}