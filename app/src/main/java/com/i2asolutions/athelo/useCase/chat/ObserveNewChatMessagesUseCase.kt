package com.i2asolutions.athelo.useCase.chat

import com.i2asolutions.athelo.presentation.model.chat.ConversationInfo
import com.i2asolutions.athelo.websocket.WebSocketManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveNewChatMessagesUseCase @Inject constructor(private val webSocketManager: WebSocketManager) {
    operator fun invoke(chatId: String): Flow<ConversationInfo.ConversationMessage> {
        return webSocketManager.observeNewMessages(chatId)
    }
}