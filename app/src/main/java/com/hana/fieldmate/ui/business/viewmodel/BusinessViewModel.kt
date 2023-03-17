package com.hana.fieldmate.ui.business.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.FieldMateScreen
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.domain.*
import com.hana.fieldmate.domain.model.BusinessEntity
import com.hana.fieldmate.domain.model.MemberNameEntity
import com.hana.fieldmate.domain.model.TaskEntity
import com.hana.fieldmate.domain.model.TaskStatisticEntity
import com.hana.fieldmate.domain.usecase.*
import com.hana.fieldmate.network.di.NetworkLoadingState
import com.hana.fieldmate.ui.DialogAction
import com.hana.fieldmate.ui.DialogState
import com.hana.fieldmate.ui.Event
import com.hana.fieldmate.util.BAD_REQUEST_ERROR_MESSAGE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class BusinessUiState(
    val business: BusinessEntity = BusinessEntity(
        0L,
        "",
        "",
        LocalDate.now(),
        LocalDate.now(),
        emptyList(),
        "",
        ""
    ),
    val businessLoadingState: NetworkLoadingState = NetworkLoadingState.SUCCESS,

    val memberNameList: List<MemberNameEntity> = emptyList(),
    val memberNameListLoadingState: NetworkLoadingState = NetworkLoadingState.SUCCESS,

    val taskList: List<TaskEntity> = emptyList(),
    val taskListLoadingState: NetworkLoadingState = NetworkLoadingState.SUCCESS,

    val taskStatisticList: List<TaskStatisticEntity> = emptyList(),
    val taskStatisticListLoadingState: NetworkLoadingState = NetworkLoadingState.SUCCESS
)

@HiltViewModel
class BusinessViewModel @Inject constructor(
    private val fetchBusinessByIdUseCase: FetchBusinessByIdUseCase,
    private val createBusinessUseCase: CreateBusinessUseCase,
    private val updateBusinessUseCase: UpdateBusinessUseCase,
    private val deleteBusinessUseCase: DeleteBusinessUseCase,
    private val fetchMemberListUseCase: FetchMemberListUseCase,
    private val fetchTaskGraphByBusinessIdUseCase: FetchTaskGraphByBusinessIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(BusinessUiState())
    val uiState: StateFlow<BusinessUiState> = _uiState.asStateFlow()

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    private val _selectedMemberListEntity = mutableStateListOf<MemberNameEntity>()
    val selectedMemberList = _selectedMemberListEntity

    val businessId: Long? = savedStateHandle["businessId"]
    val clientId: Long? = savedStateHandle["clientId"]

    fun sendEvent(event: Event) {
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }

    fun loadBusiness() {
        if (businessId != null) {
            viewModelScope.launch {
                fetchBusinessByIdUseCase(businessId)
                    .onStart { _uiState.update { it.copy(businessLoadingState = NetworkLoadingState.LOADING) } }
                    .collect { result ->
                        if (result is ResultWrapper.Success) {
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
                        } else if (result is ResultWrapper.Error) {
                            _uiState.update {
                                it.copy(
                                    businessLoadingState = NetworkLoadingState.FAILED,
                                    memberNameListLoadingState = NetworkLoadingState.FAILED
                                )
                            }
                            if (result.errorMessage != BAD_REQUEST_ERROR_MESSAGE) {
                                sendEvent(
                                    Event.Dialog(
                                        DialogState.JwtExpired,
                                        DialogAction.Open,
                                        result.errorMessage
                                    )
                                )
                            } else {
                                sendEvent(
                                    Event.Dialog(
                                        DialogState.Error,
                                        DialogAction.Open,
                                        result.errorMessage
                                    )
                                )
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
                    if (result is ResultWrapper.Success) {
                        sendEvent(Event.NavigateUp)
                    } else if (result is ResultWrapper.Error) {
                        if (result.errorMessage != BAD_REQUEST_ERROR_MESSAGE) {
                            sendEvent(
                                Event.NavigatePopUpTo(
                                    destination = FieldMateScreen.Login.name,
                                    popUpDestination = FieldMateScreen.Login.name,
                                    inclusive = true,
                                    launchOnSingleTop = true
                                )
                            )
                        }
                        sendEvent(
                            Event.Dialog(
                                DialogState.Error,
                                DialogAction.Open,
                                result.errorMessage
                            )
                        )
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
                    if (result is ResultWrapper.Success) {
                        sendEvent(Event.NavigateUp)
                    } else if (result is ResultWrapper.Error) {
                        if (result.errorMessage != BAD_REQUEST_ERROR_MESSAGE) {
                            sendEvent(
                                Event.NavigatePopUpTo(
                                    destination = FieldMateScreen.Login.name,
                                    popUpDestination = FieldMateScreen.Login.name,
                                    inclusive = true,
                                    launchOnSingleTop = true
                                )
                            )
                        }
                        sendEvent(
                            Event.Dialog(
                                DialogState.Error,
                                DialogAction.Open,
                                result.errorMessage
                            )
                        )
                    }
                }
        }
    }

    fun deleteBusiness() {
        viewModelScope.launch {
            deleteBusinessUseCase(businessId!!)
                .onStart { _uiState.update { it.copy(businessLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        sendEvent(Event.NavigateUp)
                    } else if (result is ResultWrapper.Error) {
                        if (result.errorMessage != BAD_REQUEST_ERROR_MESSAGE) {
                            sendEvent(
                                Event.NavigatePopUpTo(
                                    destination = FieldMateScreen.Login.name,
                                    popUpDestination = FieldMateScreen.Login.name,
                                    inclusive = true,
                                    launchOnSingleTop = true
                                )
                            )
                        }
                        sendEvent(
                            Event.Dialog(
                                DialogState.Error,
                                DialogAction.Open,
                                result.errorMessage
                            )
                        )
                    }
                }
        }
    }

    fun loadCompanyMembers(companyId: Long, name: String? = null) {
        viewModelScope.launch {
            fetchMemberListUseCase(companyId, name)
                .onStart { _uiState.update { it.copy(memberNameListLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        result.data.let { memberListRes ->
                            _uiState.update {
                                it.copy(
                                    memberNameList = memberListRes.toMemberEntityList()
                                        .toMemberNameEntities(),
                                    memberNameListLoadingState = NetworkLoadingState.SUCCESS
                                )
                            }
                        }
                    } else if (result is ResultWrapper.Error) {
                        _uiState.update {
                            it.copy(
                                memberNameListLoadingState = NetworkLoadingState.FAILED
                            )
                        }
                        if (result.errorMessage != BAD_REQUEST_ERROR_MESSAGE) {
                            sendEvent(
                                Event.NavigatePopUpTo(
                                    destination = FieldMateScreen.Login.name,
                                    popUpDestination = FieldMateScreen.Login.name,
                                    inclusive = true,
                                    launchOnSingleTop = true
                                )
                            )
                        }
                        sendEvent(
                            Event.Dialog(
                                DialogState.Error,
                                DialogAction.Open,
                                result.errorMessage
                            )
                        )
                    }
                }
        }
    }

    fun loadTaskGraph() {
        viewModelScope.launch {
            fetchTaskGraphByBusinessIdUseCase(businessId!!).collect { result ->
                if (result is ResultWrapper.Success) {
                    result.data.let { taskGraphRes ->
                        _uiState.update {
                            it.copy(
                                taskStatisticList = taskGraphRes.toTaskStatisticList(),
                                taskStatisticListLoadingState = NetworkLoadingState.SUCCESS
                            )
                        }
                    }
                } else if (result is ResultWrapper.Error) {
                    _uiState.update {
                        it.copy(
                            taskStatisticListLoadingState = NetworkLoadingState.FAILED
                        )
                    }
                    if (result.errorMessage != BAD_REQUEST_ERROR_MESSAGE) {
                        sendEvent(
                            Event.NavigatePopUpTo(
                                destination = FieldMateScreen.Login.name,
                                popUpDestination = FieldMateScreen.Login.name,
                                inclusive = true,
                                launchOnSingleTop = true
                            )
                        )
                    }
                    sendEvent(
                        Event.Dialog(
                            DialogState.Error,
                            DialogAction.Open,
                            result.errorMessage
                        )
                    )
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
                _uiState.value.business.revenue.toLong(),
                _uiState.value.business.description
            )
                .onStart { _uiState.update { it.copy(businessLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        sendEvent(Event.NavigateUp)
                    } else if (result is ResultWrapper.Error) {
                        if (result.errorMessage != BAD_REQUEST_ERROR_MESSAGE) {
                            sendEvent(
                                Event.NavigatePopUpTo(
                                    destination = FieldMateScreen.Login.name,
                                    popUpDestination = FieldMateScreen.Login.name,
                                    inclusive = true,
                                    launchOnSingleTop = true
                                )
                            )
                        }
                        sendEvent(
                            Event.Dialog(
                                DialogState.Error,
                                DialogAction.Open,
                                result.errorMessage
                            )
                        )
                    }
                }
        }
    }

    private fun selectMembers(memberNameList: List<MemberNameEntity>) {
        _selectedMemberListEntity.clear()
        _selectedMemberListEntity.addAll(memberNameList)
    }

    fun selectMember(memberNameEntity: MemberNameEntity) {
        _selectedMemberListEntity.add(memberNameEntity)
    }

    fun removeMember(memberNameEntity: MemberNameEntity) {
        _selectedMemberListEntity.remove(memberNameEntity)
    }
}