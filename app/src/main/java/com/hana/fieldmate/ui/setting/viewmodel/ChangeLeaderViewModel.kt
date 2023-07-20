package com.hana.fieldmate.ui.setting.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.data.ErrorType
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.domain.toMemberEntityList
import com.hana.fieldmate.domain.usecase.FetchMemberListUseCase
import com.hana.fieldmate.domain.usecase.UpdateMemberToLeaderUseCase
import com.hana.fieldmate.network.di.NetworkLoadingState
import com.hana.fieldmate.ui.DialogEvent
import com.hana.fieldmate.ui.member.viewmodel.MemberListUiState
import com.hana.fieldmate.ui.navigation.ComposeCustomNavigator
import com.hana.fieldmate.ui.navigation.NavigateAction
import com.hana.fieldmate.ui.navigation.NavigateActions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeLeaderViewModel @Inject constructor(
    private val fetchMemberListUseCase: FetchMemberListUseCase,
    private val updateMemberToLeaderUseCase: UpdateMemberToLeaderUseCase,
    private val navigator: ComposeCustomNavigator
) : ViewModel() {
    private val _uiState = MutableStateFlow(MemberListUiState())
    val uiState: StateFlow<MemberListUiState> = _uiState.asStateFlow()

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
                                dialog = DialogEvent.Error(result.error)
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
                            _uiState.update {
                                it.copy(
                                    dialog = DialogEvent.Error(
                                        ErrorType.JwtExpired("리더가 변경되었습니다\n다시 로그인해주세요")
                                    )
                                )
                            }
                        }
                        is ResultWrapper.Error -> {
                            _uiState.update {
                                it.copy(dialog = DialogEvent.Error(result.error))
                            }
                        }
                    }
                }
        }
    }

    fun openSelectLeaderDialog() {
        _uiState.update {
            it.copy(dialog = DialogEvent.Select)
        }
    }

    fun navigateTo(action: NavigateAction) {
        navigator.navigate(action)
    }

    fun backToLogin() {
        navigateTo(NavigateActions.backToLoginScreen())
    }

    fun onDialogClosed() {
        _uiState.update { it.copy(dialog = null) }
    }
}