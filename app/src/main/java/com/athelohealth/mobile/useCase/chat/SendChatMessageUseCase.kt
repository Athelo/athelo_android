package com.athelohealth.mobile.useCase.chat

import com.athelohealth.mobile.websocket.WebSocketManager
import javax.inject.Inject

class SendChatMessageUseCase @Inject constructor(private val webSocketManager: WebSocketManager) {
    operator fun invoke(chatId:String, message: String) {
        return webSocketManager.sendMessage(chatId, message)
    }
}