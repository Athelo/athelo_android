package com.athelohealth.mobile.useCase.pack

import com.athelohealth.mobile.useCase.member.LoadUserStateUseCase
import com.athelohealth.mobile.useCase.websocket.ConnectWebSocketUseCase
import com.athelohealth.mobile.useCase.websocket.DisconnectWebSocketUseCase
import javax.inject.Inject

class MainActivityUseCases @Inject constructor(
    val connectWebSocket: ConnectWebSocketUseCase,
    val disconnectWebSocket: DisconnectWebSocketUseCase,
    val loadUserState: LoadUserStateUseCase
)