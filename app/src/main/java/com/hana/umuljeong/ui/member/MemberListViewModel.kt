package com.hana.umuljeong.ui.member

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.umuljeong.data.datasource.fakeMemberData
import com.hana.umuljeong.domain.Member
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MemberListUiState(
    val memberList: List<Member> = listOf()
)


class MemberListViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MemberListUiState())
    val uiState: StateFlow<MemberListUiState> = _uiState.asStateFlow()

    init {
        loadMembers()
    }

    fun loadMembers() {
        viewModelScope.launch {
            _uiState.update { it.copy(memberList = fakeMemberData) }
        }
    }
}