package com.i2asolutions.athelo.useCase.chat

import com.i2asolutions.athelo.network.repository.chat.ChatRepository
import javax.inject.Inject

class LeaveConversationUseCase @Inject constructor(private val repository: ChatRepository) {
    suspend operator fun invoke(conversationId: Int): Boolean {
        return repository.leaveConversation(conversationId)
    }
}