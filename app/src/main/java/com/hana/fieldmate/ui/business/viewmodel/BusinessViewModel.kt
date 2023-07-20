package com.hana.fieldmate.ui.business.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.domain.*
import com.hana.fieldmate.domain.model.BusinessEntity
import com.hana.fieldmate.domain.model.MemberNameEntity
import com.hana.fieldmate.domain.model.TaskEntity
import com.hana.fieldmate.domain.model.TaskStatisticEntity
import com.hana.fieldmate.domain.usecase.*
import com.hana.fieldmate.network.di.NetworkLoadingState
import com.hana.fieldmate.ui.DialogEvent
import com.hana.fieldmate.ui.navigation.ComposeCustomNavigator
import com.hana.fieldmate.ui.navigation.EditMode
import com.hana.fieldmate.ui.navigation.NavigateAction
import com.hana.fieldmate.ui.navigation.NavigateActions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class BusinessUiState(
    val editMode: EditMode = EditMode.Add,

    val business: BusinessEntity = BusinessEntity(
        0L,
        "",
        "",
        LocalDate.now(),
        LocalDate.now(),
        emptyList(),
        "",
        0L
    ),
    val businessLoadingState: NetworkLoadingState = NetworkLoadingState.SUCCESS,

    val memberNameList: List<MemberNameEntity> = emptyList(),
    val memberNameListLoadingState: NetworkLoadingState = NetworkLoadingState.SUCCESS,

    val taskList: List<TaskEntity> = emptyList(),
    val taskListLoadingState: NetworkLoadingState = NetworkLoadingState.SUCCESS,

    val taskStatisticList: List<TaskStatisticEntity> = emptyList(),
    val taskStatisticListLoadingState: NetworkLoadingState = NetworkLoadingState.SUCCESS,

    val mode: EditMode = EditMode.Add,
    val dialog: DialogEvent? = null
)

@HiltViewModel
class BusinessViewModel @Inject constructor(
    private val fetchBusinessByIdUseCase: FetchBusinessByIdUseCase,
    private val createBusinessUseCase: CreateBusinessUseCase,
    private val updateBusinessUseCase: UpdateBusinessUseCase,
    private val deleteBusinessUseCase: DeleteBusinessUseCase,
    private val fetchMemberListUseCase: FetchMemberListUseCase,
    private val fetchTaskGraphByBusinessIdUseCase: FetchTaskGraphByBusinessIdUseCase,
    private val navigator: ComposeCustomNavigator,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(BusinessUiState())
    val uiState: StateFlow<BusinessUiState> = _uiState.asStateFlow()

    private val _selectedMemberListEntity = mutableStateListOf<MemberNameEntity>()
    val selectedMemberList = _selectedMemberListEntity

    val businessId: Long? = savedStateHandle["businessId"]
    val clientId: Long? = savedStateHandle["clientId"]

    init {
        _uiState.update {
            it.copy(mode = EditMode.valueOf(savedStateHandle["mode"] ?: "Add"))
        }
    }

    fun loadBusiness() {
        if (businessId != null) {
            viewModelScope.launch {
                fetchBusinessByIdUseCase(businessId)
                    .onStart { _uiState.update { it.copy(businessLoadingState = NetworkLoadingState.LOADING) } }
                    .collect { result ->
                        when (result) {
                            is ResultWrapper.Success -> {
                                result.data.let { businessRes ->
                                    _uiState.update {
                                        it.copy(
                                            business = businessRes.toBusinessEntity(),
                                            businessLoadingState = NetworkLoadingState.SUCCESS,
                                            memberNameListLoadingState = NetworkLoadingState.SUCCESS
                                        )
                                    }
                                    selectMembers(businessRes.memberDtoList.toMemberNameEntityList())
                                }
                            }
                            is ResultWrapper.Error -> {
                                _uiState.update {
                                    it.copy(
                                        businessLoadingState = NetworkLoadingState.FAILED,
                                        dialog = DialogEvent.Error(result.error)
                                    )
                                }
                            }
                        }
                    }
            }
        }
    }

    fun createBusiness(
        name: String,
        start: LocalDate,
        finish: LocalDate,
        revenue: Long,
        description: String
    ) {
        viewModelScope.launch {
            createBusinessUseCase(
                clientId!!,
                name,
                start,
                finish,
                selectedMemberList.map { it.id },
                revenue,
                description
            )
                .onStart { _uiState.update { it.copy(businessLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    when (result) {
                        is ResultWrapper.Success -> {
                            navigateTo(NavigateActions.navigateUp())
                        }
                        is ResultWrapper.Error -> {
                            _uiState.update {
                                it.copy(
                                    businessLoadingState = NetworkLoadingState.FAILED,
                                    dialog = DialogEvent.Error(result.error)
                                )
                            }
                        }
                    }
                }
        }
    }

    fun updateBusiness(
        name: String,
        start: LocalDate,
        finish: LocalDate,
        revenue: Long,
        description: String
    ) {
        viewModelScope.launch {
            updateBusinessUseCase(
                businessId!!,
                name,
                start,
                finish,
                selectedMemberList.map { it.id },
                revenue,
                description
            )
                .onStart { _uiState.update { it.copy(businessLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    when (result) {
                        is ResultWrapper.Success -> {
                            navigateTo(NavigateActions.navigateUp())
                        }
                        is ResultWrapper.Error -> {
                            _uiState.update {
                                it.copy(
                                    businessLoadingState = NetworkLoadingState.FAILED,
                                    dialog = DialogEvent.Error(result.error)
                                )
                            }
                        }
                    }
                }
        }
    }

    fun deleteBusiness() {
        viewModelScope.launch {
            deleteBusinessUseCase(businessId!!)
                .onStart { _uiState.update { it.copy(businessLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    when (result) {
                        is ResultWrapper.Success -> {
                            navigateTo(NavigateActions.navigateUp())
                        }
                        is ResultWrapper.Error -> {
                            _uiState.update {
                                it.copy(
                                    businessLoadingState = NetworkLoadingState.FAILED,
                                    dialog = DialogEvent.Error(result.error)
                                )
                            }
                        }
                    }
                }
        }
    }

    fun loadCompanyMembers(companyId: Long, name: String? = null) {
        viewModelScope.launch {
            fetchMemberListUseCase(companyId, name)
                .onStart { _uiState.update { it.copy(memberNameListLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    when (result) {
                        is ResultWrapper.Success -> {
                            result.data.let { memberListRes ->
                                _uiState.update {
                                    it.copy(
                                        memberNameList = memberListRes.toMemberEntityList()
                                            .toMemberNameEntities(),
                                        memberNameListLoadingState = NetworkLoadingState.SUCCESS
                                    )
                                }
                            }
                        }
                        is ResultWrapper.Error -> {
                            _uiState.update {
                                it.copy(
                                    memberNameListLoadingState = NetworkLoadingState.FAILED,
                                    dialog = DialogEvent.Error(result.error)
                                )
                            }
                        }
                    }
                }
        }
    }

    fun loadTaskGraph() {
        viewModelScope.launch {
            fetchTaskGraphByBusinessIdUseCase(businessId!!).collect { result ->
                when (result) {
                    is ResultWrapper.Success -> {
                        result.data.let { taskGraphRes ->
                            _uiState.update {
                                it.copy(
                                    taskStatisticList = taskGraphRes.toTaskStatisticList(),
                                    taskStatisticListLoadingState = NetworkLoadingState.SUCCESS
                                )
                            }
                        }
                    }
                    is ResultWrapper.Error -> {
                        _uiState.update {
                            it.copy(
                                taskStatisticListLoadingState = NetworkLoadingState.FAILED,
                                dialog = DialogEvent.Error(result.error)
                            )
                        }
                    }
                }
            }
        }
    }

    fun updateBusinessMembers() {
        viewModelScope.launch {
            updateBusinessUseCase(
                businessId!!,
                _uiState.value.business.name,
                _uiState.value.business.startDate,
                _uiState.value.business.endDate,
                selectedMemberList.map { it.id },
                _uiState.value.business.revenue,
                _uiState.value.business.description
            )
                .onStart { _uiState.update { it.copy(businessLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    when (result) {
                        is ResultWrapper.Success -> {
                            navigateTo(NavigateActions.navigateUp())
                        }
                        is ResultWrapper.Error -> {
                            _uiState.update {
                                it.copy(
                                    businessLoadingState = NetworkLoadingState.FAILED,
                                    dialog = DialogEvent.Error(result.error)
                                )
                            }
                        }
                    }
                }
        }
    }

    fun selectMembers(memberNameList: List<MemberNameEntity>) {
        _selectedMemberListEntity.clear()
        _selectedMemberListEntity.addAll(memberNameList)
    }

    fun selectMember(memberNameEntity: MemberNameEntity) {
        _selectedMemberListEntity.add(memberNameEntity)
    }

    fun removeMember(memberNameEntity: MemberNameEntity) {
        _selectedMemberListEntity.remove(memberNameEntity)
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