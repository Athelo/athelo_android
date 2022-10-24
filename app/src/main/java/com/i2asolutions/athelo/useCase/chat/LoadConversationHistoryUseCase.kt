package com.i2asolutions.athelo.useCase.chat

import com.i2asolutions.athelo.extensions.toNanoseconds
import com.i2asolutions.athelo.presentation.model.chat.ConversationInfo
import com.i2asolutions.athelo.websocket.WebSocketManager
import java.util.*
import javax.inject.Inject

class LoadConversationHistoryUseCase @Inject constructor(private val webSocketManager: WebSocketManager) {
    suspend operator fun invoke(
        chatId: String,
        lastMessageId: Long? = null,
        limit: Int = 100
    ): List<ConversationInfo.ConversationMessage> {
        return if (lastMessageId == null) {
            val time = Calendar.getInstance(TimeZone.getTimeZone("UTC")).time.toNanoseconds()
            webSocketManager.getChatHistory(chatId, time, limit).payload
        } else {
            webSocketManager.getChatHistory(chatId, lastMessageId, limit).payload
        }
    }
}