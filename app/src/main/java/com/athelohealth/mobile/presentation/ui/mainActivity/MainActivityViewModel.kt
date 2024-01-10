package com.athelohealth.mobile.presentation.ui.mainActivity

import androidx.lifecycle.viewModelScope
import com.athelohealth.mobile.presentation.model.member.AuthorizationState
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.pack.MainActivityUseCases
import com.athelohealth.mobile.useCase.patients.ObserveGlobalMessageUseCase
import com.athelohealth.mobile.utils.message.LocalMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    observeGlobalMessage: ObserveGlobalMessageUseCase,
    private val useCases: MainActivityUseCases,
) : BaseViewModel<MainActivityEvent, MainActivityEffect, MainActivityViewState>(MainActivityViewState()) {

    init {
        observeGlobalMessage().onEach { message ->
            Timber.d("Message Receive: $message")
            when (message) {
                is LocalMessage.Success -> successMessage(message = message.message)
                is LocalMessage.Error -> errorMessage(message = message.message)
                is LocalMessage.Normal -> normalMessage(message = message.message)
            }
        }.launchIn(viewModelScope)
    }

    override fun pauseLoadingState() { notifyStateChange(currentState.copy(isLoading = false)) }

    override fun loadData() {}

    override fun handleEvent(event: MainActivityEvent) = when (event) {
        MainActivityEvent.DisconnectWebSockets -> disconnectWebSockets()
        MainActivityEvent.ConnectWebSockets -> connectWebSockets()
        is MainActivityEvent.ErrorMessageReceive -> errorMessage(event.message)
        is MainActivityEvent.NormalMessageReceive -> normalMessage(event.message)
        is MainActivityEvent.SuccessMessageReceive -> successMessage(event.message)
    }

    private fun disconnectWebSockets() = launchRequest {
        useCases.disconnectWebSocket()
    }

    private fun connectWebSockets() = launchRequest {
        if (useCases.loadUserState().value == AuthorizationState.Authorized) {
            useCases.connectWebSocket()
        }
    }
}