package com.hana.fieldmate.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.FieldMateScreen
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.data.remote.repository.CompanyRepository
import com.hana.fieldmate.ui.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyViewModel @Inject constructor(
    private val companyRepository: CompanyRepository
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
            companyRepository.createCompany(name)
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        sendEvent(
                            Event.NavigatePopUpTo(
                                destination = FieldMateScreen.Report.name,
                                popUpDestination = FieldMateScreen.Login.name,
                                inclusive = true
                            )
                        )
                    } else {
                        // TODO : 에러 처리
                    }
                }
        }
    }

    fun joinCompany() {
        viewModelScope.launch {
            companyRepository.joinCompany()
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        sendEvent(
                            Event.NavigatePopUpTo(
                                destination = FieldMateScreen.Report.name,
                                popUpDestination = FieldMateScreen.Login.name,
                                inclusive = true
                            )
                        )
                    }
                }
        }
    }
}