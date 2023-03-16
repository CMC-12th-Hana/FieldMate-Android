package com.hana.fieldmate.ui.client.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.FieldMateScreen
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.model.request.SalesRepresentative
import com.hana.fieldmate.data.remote.model.request.UpdateClientReq
import com.hana.fieldmate.domain.model.BusinessEntity
import com.hana.fieldmate.domain.model.ClientEntity
import com.hana.fieldmate.domain.model.TaskStatisticEntity
import com.hana.fieldmate.domain.toBusinessEntityList
import com.hana.fieldmate.domain.toClientEntity
import com.hana.fieldmate.domain.toTaskStatisticList
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
import javax.inject.Inject

data class ClientUiState(
    val client: ClientEntity = ClientEntity(-1L, "", "", "", "", "", 0, 0),
    val clientLoadingState: NetworkLoadingState = NetworkLoadingState.SUCCESS,

    val businessList: List<BusinessEntity> = emptyList(),
    val businessListLoadingState: NetworkLoadingState = NetworkLoadingState.SUCCESS,

    val taskStatisticList: List<TaskStatisticEntity> = emptyList(),
    val taskStatisticListLoadingState: NetworkLoadingState = NetworkLoadingState.SUCCESS
)

@HiltViewModel
class ClientViewModel @Inject constructor(
    private val fetchClientByIdUseCase: FetchClientByIdUseCase,
    private val createClientUseCase: CreateClientUseCase,
    private val updateClientUseCase: UpdateClientUseCase,
    private val deleteClientUseCase: DeleteClientUseCase,
    private val fetchTaskGraphByClientIdUseCase: FetchTaskGraphByClientIdUseCase,
    private val fetchBusinessListByClientIdUseCase: FetchBusinessListByClientIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(ClientUiState())
    val uiState: StateFlow<ClientUiState> = _uiState.asStateFlow()

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    val clientId: Long? = savedStateHandle["clientId"]

    fun loadTaskGraph() {
        viewModelScope.launch {
            fetchTaskGraphByClientIdUseCase(clientId!!).collect { result ->
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

    fun sendEvent(event: Event) {
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }

    fun loadClient() {
        if (clientId != null) {
            viewModelScope.launch {
                fetchClientByIdUseCase(clientId)
                    .onStart { _uiState.update { it.copy(clientLoadingState = NetworkLoadingState.LOADING) } }
                    .collect { result ->
                        if (result is ResultWrapper.Success) {
                            result.data.let { clientRes ->
                                _uiState.update {
                                    it.copy(
                                        client = clientRes.toClientEntity(),
                                        clientLoadingState = NetworkLoadingState.SUCCESS
                                    )
                                }
                            }
                        } else if (result is ResultWrapper.Error) {
                            _uiState.update {
                                it.copy(clientLoadingState = NetworkLoadingState.FAILED)
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
    }

    fun createClient(
        companyId: Long,
        name: String,
        tel: String,
        srName: String,
        srPhoneNumber: String,
        srDepartment: String
    ) {
        viewModelScope.launch {
            createClientUseCase(companyId, name, tel, srName, srPhoneNumber, srDepartment)
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

    fun updateClient(
        name: String,
        tel: String,
        srName: String,
        srPhoneNumber: String,
        srDepartment: String
    ) {
        viewModelScope.launch {
            updateClientUseCase(
                clientId!!,
                UpdateClientReq(
                    name,
                    tel,
                    SalesRepresentative(srName, srPhoneNumber, srDepartment)
                )
            )
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

    fun deleteClient() {
        viewModelScope.launch {
            deleteClientUseCase(clientId!!)
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

    fun loadBusinesses(name: String?, start: String?, finish: String?) {
        viewModelScope.launch {
            fetchBusinessListByClientIdUseCase(clientId!!, name, start, finish)
                .onStart { _uiState.update { it.copy(businessListLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        result.data.let { businessListRes ->
                            _uiState.update {
                                it.copy(
                                    businessList = businessListRes.toBusinessEntityList(),
                                    businessListLoadingState = NetworkLoadingState.SUCCESS
                                )
                            }
                        }
                    } else if (result is ResultWrapper.Error) {
                        _uiState.update { it.copy(businessListLoadingState = NetworkLoadingState.FAILED) }
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
}