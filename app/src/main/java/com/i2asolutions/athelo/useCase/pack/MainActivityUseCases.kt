package com.i2asolutions.athelo.useCase.pack

import com.i2asolutions.athelo.useCase.member.LoadUserStateUseCase
import com.i2asolutions.athelo.useCase.websocket.ConnectWebSocketUseCase
import com.i2asolutions.athelo.useCase.websocket.DisconnectWebSocketUseCase
import javax.inject.Inject

class MainActivityUseCases @Inject constructor(
    val connectWebSocket: ConnectWebSocketUseCase,
    val disconnectWebSocket: DisconnectWebSocketUseCase,
    val loadUserState: LoadUserStateUseCase
)