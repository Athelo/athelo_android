package com.athelohealth.mobile.useCase.chat

import com.athelohealth.mobile.network.repository.chat.ChatRepository
import javax.inject.Inject

class JoinConversationUseCase @Inject constructor(private val repository: ChatRepository) {
    suspend operator fun invoke(conversationId: Int): Boolean {
        return repository.joinConversation(conversationId)
    }
}