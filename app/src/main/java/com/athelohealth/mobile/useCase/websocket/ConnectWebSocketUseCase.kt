package com.athelohealth.mobile.useCase.websocket

import com.athelohealth.mobile.websocket.WebSocketManager
import javax.inject.Inject

class ConnectWebSocketUseCase @Inject constructor(private val webSocketManager: WebSocketManager) {

    suspend operator fun invoke() = webSocketManager.connect()
}