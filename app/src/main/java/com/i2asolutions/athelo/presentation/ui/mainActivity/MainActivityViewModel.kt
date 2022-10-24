package com.i2asolutions.athelo.presentation.ui.mainActivity

import com.i2asolutions.athelo.presentation.model.member.AuthorizationState
import com.i2asolutions.athelo.presentation.ui.base.BaseViewModel
import com.i2asolutions.athelo.useCase.pack.MainActivityUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val useCases: MainActivityUseCases) :
    BaseViewModel<MainActivityEvent, MainActivityEffect>() {

    override fun loadData() {}

    override fun handleEvent(event: MainActivityEvent) = when (event) {
        MainActivityEvent.DisconnectWebSockets -> disconnectWebSockets()
        MainActivityEvent.ConnectWebSockets -> connectWebSockets()
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