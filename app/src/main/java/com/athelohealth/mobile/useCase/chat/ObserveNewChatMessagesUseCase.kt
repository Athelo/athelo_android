package com.athelohealth.mobile.useCase.chat

import com.athelohealth.mobile.presentation.model.chat.ConversationInfo
import com.athelohealth.mobile.websocket.WebSocketManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveNewChatMessagesUseCase @Inject constructor(private val webSocketManager: WebSocketManager) {
    operator fun invoke(chatId: String): Flow<ConversationInfo.ConversationMessage> {
        return webSocketManager.observeNewMessages(chatId)
    }
}