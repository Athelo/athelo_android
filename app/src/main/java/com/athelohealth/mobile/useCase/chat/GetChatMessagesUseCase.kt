package com.athelohealth.mobile.useCase.chat

import com.athelohealth.mobile.network.repository.chat.ChatRepository
import com.athelohealth.mobile.presentation.model.chat.ConversationInfo
import javax.inject.Inject

class GetChatMessagesUseCase @Inject constructor(private val repository: ChatRepository) {
	suspend operator fun invoke(conversationId: Int): List<ConversationInfo.ConversationMessage> {
		return repository.getChat(conversationId).results.sortedByDescending { it.id }.map { it.toConversation() }
	}
}