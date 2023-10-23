package com.i2asolutions.athelo.network.repository.chat

import com.i2asolutions.athelo.extensions.getBodyOrThrow
import com.i2asolutions.athelo.extensions.parseResponseWithoutBody
import com.i2asolutions.athelo.network.api.ChatApi
import com.i2asolutions.athelo.network.dto.base.PageResponseDto
import com.i2asolutions.athelo.network.dto.chat.ChatTokenDto
import com.i2asolutions.athelo.network.dto.chat.ConversationDto
import com.i2asolutions.athelo.network.dto.chat.PrivateConversationDto
import com.i2asolutions.athelo.network.repository.BaseRepository
import com.i2asolutions.athelo.utils.UserManager

class ChatRepositoryImpl(userManager: UserManager) :
    BaseRepository<ChatApi>(userManager = userManager, clazz = ChatApi::class.java),
    ChatRepository {

    override suspend fun openSession(deviceId: String): ChatTokenDto {
        return service.postOpenSession(
            mapOf(
                "device_identifier" to deviceId
            )
        ).getBodyOrThrow()
    }

    override suspend fun closeSession(sessionToken: String): Boolean {
        return service.postCloseSession(mapOf("token" to sessionToken)).isSuccessful
    }

    override suspend fun loadConversations(nextUrl: String?): PageResponseDto<ConversationDto> {
        return if (nextUrl == null) service.getGroupConversations() else service.getGroupConversations(
            nextUrl
        )
    }

    override suspend fun createPrivateConversations(userId: String): PrivateConversationDto {
        return service.postCreateConversation(mapOf("user_profile_id" to userId)).getBodyOrThrow()
    }

    override suspend fun loadPrivateConversations(
        nextUrl: String?,
        userId: List<Int>
    ): PageResponseDto<PrivateConversationDto> {
        return if (nextUrl == null) {
            service.getPrivateConversations(
                forUsersIds = if (userId.isEmpty()) null else userId.joinToString(",")
            )
        } else {
            service.getPrivateConversations(nextUrl)
        }
    }

    override suspend fun loadPrivateConversation(conversationId: Int): PrivateConversationDto {
        return service.getConversation(conversationId)
    }

    override suspend fun loadConversation(conversationId: Int): ConversationDto {
        return service.getGroupConversation(conversationId)
    }

    override suspend fun joinConversation(conversationId: Int): Boolean {
        return service.getJoinConversation(conversationId = conversationId)
            .parseResponseWithoutBody()
    }

    override suspend fun leaveConversation(conversationId: Int): Boolean {
        return service.getLeaveConversation(conversationId = conversationId)
            .parseResponseWithoutBody()
    }

}