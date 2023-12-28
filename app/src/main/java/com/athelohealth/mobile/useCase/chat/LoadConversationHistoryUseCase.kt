package com.athelohealth.mobile.useCase.chat

import com.athelohealth.mobile.extensions.toNanoseconds
import com.athelohealth.mobile.presentation.model.chat.ConversationInfo
import com.athelohealth.mobile.websocket.WebSocketManager
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