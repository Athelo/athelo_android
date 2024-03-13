package com.athelohealth.mobile.network.repository.chat

import com.athelohealth.mobile.extensions.getBodyOrThrow
import com.athelohealth.mobile.extensions.parseResponseWithoutBody
import com.athelohealth.mobile.network.api.ChatApi
import com.athelohealth.mobile.network.dto.application.FAQDto
import com.athelohealth.mobile.network.dto.base.PageResponseDto
import com.athelohealth.mobile.network.dto.chat.ChatMessages
import com.athelohealth.mobile.network.dto.chat.ChatTokenDto
import com.athelohealth.mobile.network.dto.chat.ConversationDto
import com.athelohealth.mobile.network.dto.chat.PrivateConversationDto
import com.athelohealth.mobile.network.repository.BaseRepository
import com.athelohealth.mobile.utils.UserManager

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

    override suspend fun getChat(conversationId: Int): PageResponseDto<ChatMessages> {
        return service.getCommunityThreads(conversationId)
    }

    override suspend fun putChat(conversationId: Int, content: String): ChatMessages {
        return service.putCommunityThreads(conversationId, FAQDto(content = content))
    }
}