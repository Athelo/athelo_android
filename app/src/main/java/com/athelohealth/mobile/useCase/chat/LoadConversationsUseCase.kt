package com.athelohealth.mobile.useCase.chat

import com.athelohealth.mobile.network.repository.chat.ChatRepository
import com.athelohealth.mobile.presentation.model.base.PageResponse
import com.athelohealth.mobile.presentation.model.chat.Conversation
import javax.inject.Inject

class LoadConversationsUseCase @Inject constructor(private val repository: ChatRepository) {
    suspend operator fun invoke(url: String? = null): PageResponse<Conversation> {
        return repository.loadConversations(url).toPageResponse { it.toConversation() }
    }
}