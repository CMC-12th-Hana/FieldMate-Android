package com.hana.fieldmate.ui.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.FieldMateScreen
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.domain.usecase.CreateCompanyUseCase
import com.hana.fieldmate.domain.usecase.JoinCompanyUseCase
import com.hana.fieldmate.ui.DialogAction
import com.hana.fieldmate.ui.DialogState
import com.hana.fieldmate.ui.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyViewModel @Inject constructor(
    private val createCompanyUseCase: CreateCompanyUseCase,
    private val joinCompanyUseCase: JoinCompanyUseCase
) : ViewModel() {
    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    fun sendEvent(event: Event) {
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }

    fun createCompany(name: String) {
        viewModelScope.launch {
            createCompanyUseCase(name)
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        sendEvent(
                            Event.NavigatePopUpTo(
                                destination = FieldMateScreen.TaskList.name,
                                popUpDestination = FieldMateScreen.Login.name,
                                inclusive = true
                            )
                        )
                    } else if (result is ResultWrapper.Error) {
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

    fun joinCompany() {
        viewModelScope.launch {
            joinCompanyUseCase()
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        sendEvent(
                            Event.NavigatePopUpTo(
                                destination = FieldMateScreen.TaskList.name,
                                popUpDestination = FieldMateScreen.Login.name,
                                inclusive = true
                            )
                        )
                    } else if (result is ResultWrapper.Error) {
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