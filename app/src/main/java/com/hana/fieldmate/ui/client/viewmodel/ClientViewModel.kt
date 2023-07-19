package com.hana.fieldmate.ui.client.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.data.ErrorType
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
import com.hana.fieldmate.ui.DialogType
import com.hana.fieldmate.ui.navigation.ComposeCustomNavigator
import com.hana.fieldmate.ui.navigation.EditMode
import com.hana.fieldmate.ui.navigation.NavigateAction
import com.hana.fieldmate.ui.navigation.NavigateActions
import com.hana.fieldmate.util.PHONE_NUMBER_INVALID_MESSAGE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ClientUiState(
    val client: ClientEntity = ClientEntity(-1L, "", "", "", "", "", 0, 0),
    val clientLoadingState: NetworkLoadingState = NetworkLoadingState.SUCCESS,

    val businessList: List<BusinessEntity> = emptyList(),
    val businessListLoadingState: NetworkLoadingState = NetworkLoadingState.SUCCESS,

    val taskStatisticList: List<TaskStatisticEntity> = emptyList(),
    val taskStatisticListLoadingState: NetworkLoadingState = NetworkLoadingState.SUCCESS,

    val mode: EditMode = EditMode.Add,
    val dialog: DialogType? = null
)

@HiltViewModel
class ClientViewModel @Inject constructor(
    private val fetchClientByIdUseCase: FetchClientByIdUseCase,
    private val createClientUseCase: CreateClientUseCase,
    private val updateClientUseCase: UpdateClientUseCase,
    private val deleteClientUseCase: DeleteClientUseCase,
    private val fetchTaskGraphByClientIdUseCase: FetchTaskGraphByClientIdUseCase,
    private val fetchBusinessListByClientIdUseCase: FetchBusinessListByClientIdUseCase,
    private val navigator: ComposeCustomNavigator,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(ClientUiState())
    val uiState: StateFlow<ClientUiState> = _uiState.asStateFlow()

    val clientId: Long? = savedStateHandle["clientId"]

    init {
        _uiState.update {
            it.copy(mode = EditMode.valueOf(savedStateHandle["mode"] ?: "Add"))
        }
    }

    fun loadTaskGraph() {
        viewModelScope.launch {
            fetchTaskGraphByClientIdUseCase(clientId!!).collect { result ->
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
                                dialog = DialogType.Error(result.error)
                            )
                        }
                    }
                }
            }
        }
    }

    fun loadClient() {
        if (clientId != null) {
            viewModelScope.launch {
                fetchClientByIdUseCase(clientId)
                    .onStart { _uiState.update { it.copy(clientLoadingState = NetworkLoadingState.LOADING) } }
                    .collect { result ->
                        when (result) {
                            is ResultWrapper.Success -> {
                                result.data.let { clientRes ->
                                    _uiState.update {
                                        it.copy(
                                            client = clientRes.toClientEntity(),
                                            clientLoadingState = NetworkLoadingState.SUCCESS
                                        )
                                    }
                                }
                            }
                            is ResultWrapper.Error -> {
                                _uiState.update {
                                    it.copy(
                                        clientLoadingState = NetworkLoadingState.FAILED,
                                        dialog = DialogType.Error(result.error)
                                    )
                                }
                            }
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
                .onStart { _uiState.update { it.copy(clientLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    when (result) {
                        is ResultWrapper.Success -> {
                            navigateTo(NavigateActions.navigateUp())
                        }
                        is ResultWrapper.Error -> {
                            _uiState.update {
                                it.copy(
                                    clientLoadingState = NetworkLoadingState.FAILED,
                                    dialog = DialogType.Error(result.error)
                                )
                            }
                        }
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
                .onStart { _uiState.update { it.copy(clientLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    when (result) {
                        is ResultWrapper.Success -> {
                            navigateTo(NavigateActions.navigateUp())
                        }
                        is ResultWrapper.Error -> {
                            _uiState.update {
                                it.copy(
                                    clientLoadingState = NetworkLoadingState.FAILED,
                                    dialog = DialogType.Error(result.error)
                                )
                            }
                        }
                    }
                }
        }
    }

    fun deleteClient() {
        viewModelScope.launch {
            deleteClientUseCase(clientId!!)
                .onStart { _uiState.update { it.copy(clientLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    when (result) {
                        is ResultWrapper.Success -> {
                            navigateTo(NavigateActions.navigateUp())
                        }
                        is ResultWrapper.Error -> {
                            _uiState.update {
                                it.copy(
                                    clientLoadingState = NetworkLoadingState.FAILED,
                                    dialog = DialogType.Error(result.error)
                                )
                            }
                        }
                    }
                }
        }
    }

    fun loadBusinesses(name: String?, start: String?, finish: String?) {
        viewModelScope.launch {
            fetchBusinessListByClientIdUseCase(clientId!!, name, start, finish)
                .onStart { _uiState.update { it.copy(businessListLoadingState = NetworkLoadingState.LOADING) } }
                .collect { result ->
                    when (result) {
                        is ResultWrapper.Success -> {
                            result.data.let { businessListRes ->
                                _uiState.update {
                                    it.copy(
                                        businessList = businessListRes.toBusinessEntityList(),
                                        businessListLoadingState = NetworkLoadingState.SUCCESS
                                    )
                                }
                            }
                        }
                        is ResultWrapper.Error -> {
                            _uiState.update {
                                it.copy(
                                    businessListLoadingState = NetworkLoadingState.FAILED,
                                    dialog = DialogType.Error(result.error)
                                )
                            }
                        }
                    }
                }
        }
    }

    fun openPhoneNumberErrorDialog() {
        _uiState.update {
            it.copy(
                dialog = DialogType.Error(
                    ErrorType.General(PHONE_NUMBER_INVALID_MESSAGE)
                )
            )
        }
    }

    fun openDeleteDialog() {
        _uiState.update {
            it.copy(dialog = DialogType.Delete)
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