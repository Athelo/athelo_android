package com.athelohealth.mobile.useCase.chat

import com.athelohealth.mobile.network.repository.chat.ChatRepository
import com.athelohealth.mobile.presentation.model.chat.Conversation
import com.athelohealth.mobile.utils.consts.Const
import javax.inject.Inject

class LoadConversationDetailUseCase @Inject constructor(private val repository: ChatRepository) {
    suspend operator fun invoke(conversationId: Int, isGroupConversation: Boolean): Conversation {
        val conversation = if (isGroupConversation) repository.loadConversation(conversationId)
            .toConversation() else repository.loadPrivateConversation(conversationId)
            .toConversation()
        return conversation ?: throw NullPointerException(Const.UNIVERSAL_ERROR_MESSAGE)
    }
}