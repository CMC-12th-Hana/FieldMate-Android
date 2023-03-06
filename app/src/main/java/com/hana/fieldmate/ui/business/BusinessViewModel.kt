package com.hana.fieldmate.ui.business

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.data.local.fakeBusinessDataSource
import com.hana.fieldmate.domain.model.BusinessEntity
import com.hana.fieldmate.domain.model.MemberEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

data class BusinessUiState(
    val businessEntity: BusinessEntity = BusinessEntity(
        0L,
        "",
        LocalDate.now(),
        LocalDate.now(),
        emptyList(),
        "",
        ""
    )
)

class BusinessViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(BusinessUiState())
    val uiState: StateFlow<BusinessUiState> = _uiState.asStateFlow()

    private val _selectedMemberListEntity = mutableStateListOf<MemberEntity>()
    val selectedMemberList = _selectedMemberListEntity

    init {
        val id: Long? = savedStateHandle["businessId"]
        if (id != null) loadBusiness(id)
    }

    fun loadBusiness(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(businessEntity = fakeBusinessDataSource[id.toInt()]) }
            _selectedMemberListEntity.addAll(_uiState.value.businessEntity.memberEntities)
        }
    }

    fun selectedMembers(selectedMemberEntities: List<MemberEntity>) {
        _selectedMemberListEntity.addAll(selectedMemberEntities)
    }

    fun removeMember(memberEntity: MemberEntity) {
        _selectedMemberListEntity.remove(memberEntity)
    }
}