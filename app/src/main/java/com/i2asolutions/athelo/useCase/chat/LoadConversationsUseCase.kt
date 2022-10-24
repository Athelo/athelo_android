package com.i2asolutions.athelo.useCase.chat

import com.i2asolutions.athelo.network.repository.chat.ChatRepository
import com.i2asolutions.athelo.presentation.model.base.PageResponse
import com.i2asolutions.athelo.presentation.model.chat.Conversation
import javax.inject.Inject

class LoadConversationsUseCase @Inject constructor(private val repository: ChatRepository) {
    suspend operator fun invoke(url: String? = null): PageResponse<Conversation> {
        return repository.loadConversations(url).toPageResponse { it.toConversation() }
    }
}