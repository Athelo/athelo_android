package com.i2asolutions.athelo.useCase.chat

import com.i2asolutions.athelo.network.repository.chat.ChatRepository
import com.i2asolutions.athelo.presentation.model.chat.Conversation
import com.i2asolutions.athelo.utils.consts.Const
import javax.inject.Inject

class LoadConversationDetailUseCase @Inject constructor(private val repository: ChatRepository) {
    suspend operator fun invoke(conversationId: Int): Conversation {
        return repository.loadConversation(conversationId).toConversation() ?: throw NullPointerException(Const.UNIVERSAL_ERROR_MESSAGE)
    }
}