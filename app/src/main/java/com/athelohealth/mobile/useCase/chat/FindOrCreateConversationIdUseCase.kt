package com.athelohealth.mobile.useCase.chat

import com.athelohealth.mobile.network.dto.chat.PrivateConversationDto
import com.athelohealth.mobile.network.repository.chat.ChatRepository
import javax.inject.Inject

class FindOrCreateConversationIdUseCase @Inject constructor(
    private val repository: ChatRepository,
) {

    suspend operator fun invoke(userId: String): Int? {
        val conversation =
            findConversation(userId.toInt(), null) ?: repository.createPrivateConversations(userId)
        return conversation.id
    }

    private suspend fun findConversation(
        userId: Int,
        nextUrl: String? = null
    ): PrivateConversationDto? {
        val result = repository.loadPrivateConversations(nextUrl, listOf(userId))
        val conversation =
            result.results.firstOrNull { it.user_profile?.id == userId }
        if (conversation != null) return conversation
        if (result.nextUrl == null) return null
        return findConversation(userId, result.nextUrl)
    }
}