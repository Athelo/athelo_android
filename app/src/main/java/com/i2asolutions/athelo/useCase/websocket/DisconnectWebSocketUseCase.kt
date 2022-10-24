package com.i2asolutions.athelo.useCase.websocket

import com.i2asolutions.athelo.websocket.WebSocketManager
import javax.inject.Inject

class DisconnectWebSocketUseCase @Inject constructor(private val webSocketManager: WebSocketManager) {

    suspend operator fun invoke() = webSocketManager.disconnect()
}