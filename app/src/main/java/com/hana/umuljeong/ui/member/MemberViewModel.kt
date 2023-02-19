package com.hana.umuljeong.ui.member

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.umuljeong.R
import com.hana.umuljeong.data.datasource.fakeMemberData
import com.hana.umuljeong.data.model.Member
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MemberUiState(
    val member: Member = Member(0L, R.drawable.ic_member_profile, "", "", "", "", "")
)

class MemberViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(MemberUiState())
    val uiState: StateFlow<MemberUiState> = _uiState.asStateFlow()

    init {
        val id: Long = savedStateHandle["memberId"]!!
        if (id != -1L) loadMember(id)
    }

    fun loadMember(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(member = fakeMemberData[id.toInt()]) }
        }
    }
}