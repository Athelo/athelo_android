package com.athelohealth.mobile.network.repository.chat

import com.athelohealth.mobile.network.dto.base.PageResponseDto
import com.athelohealth.mobile.network.dto.chat.ChatMessages
import com.athelohealth.mobile.network.dto.chat.ChatTokenDto
import com.athelohealth.mobile.network.dto.chat.ConversationDto
import com.athelohealth.mobile.network.dto.chat.PrivateConversationDto

interface ChatRepository {
    suspend fun openSession(deviceId: String): ChatTokenDto
    suspend fun closeSession(sessionToken: String): Boolean

    suspend fun loadConversations(nextUrl: String?): PageResponseDto<ConversationDto>
    suspend fun createPrivateConversations(userId: String): PrivateConversationDto
    suspend fun loadPrivateConversations(
        nextUrl: String?,
        userId: List<Int>
    ): PageResponseDto<PrivateConversationDto>

    suspend fun loadPrivateConversation(conversationId: Int): PrivateConversationDto
    suspend fun loadConversation(conversationId: Int): ConversationDto
    suspend fun joinConversation(conversationId: Int): Boolean
    suspend fun leaveConversation(conversationId: Int): Boolean
    suspend fun getChat(conversationId: Int): PageResponseDto<ChatMessages>
    suspend fun putChat(conversationId: Int, content: String): ChatMessages
}