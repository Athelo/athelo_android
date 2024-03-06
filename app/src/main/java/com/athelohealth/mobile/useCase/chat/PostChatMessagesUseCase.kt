package com.athelohealth.mobile.useCase.chat

import com.athelohealth.mobile.network.repository.chat.ChatRepository
import com.athelohealth.mobile.presentation.model.chat.ConversationInfo
import javax.inject.Inject

class PostChatMessagesUseCase @Inject constructor(private val repository: ChatRepository) {
	suspend operator fun invoke(conversationId: Int, content: String): ConversationInfo.ConversationMessage {
		return repository.putChat(conversationId, content).toConversation()
	}
}