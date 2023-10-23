package com.i2asolutions.athelo.network.repository.chat

import com.i2asolutions.athelo.network.dto.base.PageResponseDto
import com.i2asolutions.athelo.network.dto.chat.ChatTokenDto
import com.i2asolutions.athelo.network.dto.chat.ConversationDto
import com.i2asolutions.athelo.network.dto.chat.PrivateConversationDto

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
}