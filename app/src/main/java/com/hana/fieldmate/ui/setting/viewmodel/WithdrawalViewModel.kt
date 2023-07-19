package com.hana.fieldmate.ui.setting.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.App
import com.hana.fieldmate.data.ErrorType
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.domain.usecase.QuitMemberUseCase
import com.hana.fieldmate.ui.Event
import com.hana.fieldmate.ui.navigation.FieldMateScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

data class WithdrawlUiState(
    val error: ErrorType? = null
)

@HiltViewModel
class WithdrawalViewModel @Inject constructor(
    private val quitMemberUseCase: QuitMemberUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(WithdrawlUiState())
    val uiState: StateFlow<WithdrawlUiState> = _uiState.asStateFlow()

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    fun sendEvent(event: Event) {
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }

    fun quitMember() {
        viewModelScope.launch {
            quitMemberUseCase()
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        runBlocking {
                            App.getInstance().getDataStore().deleteAccessToken()
                            App.getInstance().getDataStore().deleteRefreshToken()
                        }
                        sendEvent(
                            Event.NavigatePopUpTo(
                                destination = FieldMateScreen.Login.name,
                                popUpDestination = FieldMateScreen.Login.name,
                                inclusive = true,
                                launchOnSingleTop = true
                            )
                        )
                    } else if (result is ResultWrapper.Error) {
                        _uiState.update { it.copy(error = result.error) }
                    }
                }
        }
    }
}