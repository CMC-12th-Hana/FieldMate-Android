package com.hana.fieldmate.ui.member.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.R
import com.hana.fieldmate.data.ErrorType
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.domain.model.MemberEntity
import com.hana.fieldmate.domain.toMemberEntity
import com.hana.fieldmate.domain.usecase.*
import com.hana.fieldmate.network.di.NetworkLoadingState
import com.hana.fieldmate.ui.DialogEvent
import com.hana.fieldmate.ui.navigation.ComposeCustomNavigator
import com.hana.fieldmate.ui.navigation.NavigateAction
import com.hana.fieldmate.ui.navigation.NavigateActions
import com.hana.fieldmate.util.PHONE_NUMBER_INVALID_MESSAGE
import dagger.hilt.android.lifecycle.HiltViewModel
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
    val memberLoadingState: NetworkLoadingState = NetworkLoadingState.SUCCESS,
    val dialog: DialogEvent? = null
)

@HiltViewModel
class MemberViewModel @Inject constructor(
    private val fetchProfileByIdUseCase: FetchProfileByIdUseCase,
    private val createMemberUseCase: CreateMemberUseCase,
    private val updateMyProfileUseCase: UpdateMyProfileUseCase,
    private val updateMemberProfileUseCase: UpdateMemberProfileUseCase,
    private val deleteMemberUseCase: DeleteMemberUseCase,
    private val navigator: ComposeCustomNavigator,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(MemberUiState())
    val uiState: StateFlow<MemberUiState> = _uiState.asStateFlow()

    val memberId: Long? = savedStateHandle["memberId"]

    fun loadMember() {
        if (memberId != null) {
            viewModelScope.launch {
                fetchProfileByIdUseCase(memberId)
                    .onStart { _uiState.update { it.copy(memberLoadingState = NetworkLoadingState.LOADING) } }
                    .collect { result ->
                        when (result) {
                            is ResultWrapper.Success -> {
                                result.data.let { memberRes ->
                                    _uiState.update {
                                        it.copy(
                                            member = memberRes.toMemberEntity(),
                                            memberLoadingState = NetworkLoadingState.SUCCESS
                                        )
                                    }
                                }
                            }
                            is ResultWrapper.Error -> {
                                _uiState.update {
                                    it.copy(
                                        memberLoadingState = NetworkLoadingState.FAILED,
                                        dialog = DialogEvent.Error(result.error)
                                    )
                                }
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
            createMemberUseCase(companyId, name, phoneNumber, staffRank, staffNumber)
                .onStart { _uiState.update { it.copy(memberLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    when (result) {
                        is ResultWrapper.Success -> {
                            _uiState.update {
                                it.copy(
                                    memberLoadingState = NetworkLoadingState.SUCCESS,
                                    dialog = DialogEvent.AddEdit
                                )
                            }
                        }
                        is ResultWrapper.Error -> {
                            _uiState.update {
                                it.copy(
                                    memberLoadingState = NetworkLoadingState.FAILED,
                                    dialog = DialogEvent.Error(result.error)
                                )
                            }
                        }
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
                    when (result) {
                        is ResultWrapper.Success -> {
                            navigateTo(NavigateActions.navigateUp())
                        }
                        is ResultWrapper.Error -> {
                            _uiState.update {
                                it.copy(
                                    memberLoadingState = NetworkLoadingState.FAILED,
                                    dialog = DialogEvent.Error(result.error)
                                )
                            }
                        }
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
                    when (result) {
                        is ResultWrapper.Success -> {
                            if (_uiState.value.member.phoneNumber != phoneNumber) {
                                _uiState.update {
                                    it.copy(dialog = DialogEvent.Confirm)
                                }
                            } else {
                                navigateTo(NavigateActions.navigateUp())
                            }
                        }
                        is ResultWrapper.Error -> {
                            _uiState.update {
                                it.copy(
                                    memberLoadingState = NetworkLoadingState.FAILED,
                                    dialog = DialogEvent.Error(result.error)
                                )
                            }
                        }
                    }
                }
        }
    }

    fun deleteMember() {
        viewModelScope.launch {
            deleteMemberUseCase(memberId!!)
                .onStart { _uiState.update { it.copy(memberLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    when (result) {
                        is ResultWrapper.Success -> {
                            navigateTo(NavigateActions.navigateUp())
                        }
                        is ResultWrapper.Error -> {
                            _uiState.update {
                                it.copy(
                                    memberLoadingState = NetworkLoadingState.FAILED,
                                    dialog = DialogEvent.Error(result.error)
                                )
                            }
                        }
                    }
                }
        }
    }

    fun openDeleteDialog() {
        _uiState.update {
            it.copy(dialog = DialogEvent.Delete)
        }
    }

    fun openPhoneNumberErrorDialog() {
        _uiState.update {
            it.copy(
                dialog = DialogEvent.Error(
                    ErrorType.General(PHONE_NUMBER_INVALID_MESSAGE)
                )
            )
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