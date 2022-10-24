package com.i2asolutions.athelo.useCase.chat

import com.i2asolutions.athelo.websocket.WebSocketManager
import javax.inject.Inject

class SendChatMessageUseCase @Inject constructor(private val webSocketManager: WebSocketManager) {
    operator fun invoke(chatId:String, message: String) {
        return webSocketManager.sendMessage(chatId, message)
    }
}