package com.hana.umuljeong.ui.business

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.umuljeong.data.datasource.fakeBusinessData
import com.hana.umuljeong.domain.Business
import com.hana.umuljeong.domain.Member
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BusinessUiState(
    val business: Business = Business(0L, "", "", "", emptyList(), "", "")
)

class BusinessViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(BusinessUiState())
    val uiState: StateFlow<BusinessUiState> = _uiState.asStateFlow()

    private val _selectedMemberList = mutableStateListOf<Member>()
    val selectedMemberList = _selectedMemberList

    init {
        val id: Long? = savedStateHandle["businessId"]
        if (id != null) loadBusiness(id)
    }

    fun loadBusiness(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(business = fakeBusinessData[id.toInt()]) }
            _selectedMemberList.addAll(_uiState.value.business.members)
        }
    }

    fun selectedMembers(selectedMembers: List<Member>) {
        _selectedMemberList.addAll(selectedMembers)
    }

    fun removeMember(member: Member) {
        _selectedMemberList.remove(member)
    }
}