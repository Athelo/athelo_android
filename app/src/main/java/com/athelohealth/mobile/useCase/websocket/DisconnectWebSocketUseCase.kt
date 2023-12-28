package com.athelohealth.mobile.useCase.websocket

import com.athelohealth.mobile.websocket.WebSocketManager
import javax.inject.Inject

class DisconnectWebSocketUseCase @Inject constructor(private val webSocketManager: WebSocketManager) {

    suspend operator fun invoke() = webSocketManager.disconnect()
}