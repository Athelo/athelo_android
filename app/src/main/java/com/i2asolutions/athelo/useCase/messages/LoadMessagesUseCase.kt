package com.i2asolutions.athelo.useCase.messages

import com.i2asolutions.athelo.network.repository.chat.ChatRepository
import com.i2asolutions.athelo.presentation.model.base.PageResponse
import com.i2asolutions.athelo.presentation.model.chat.PrivateConversation
import com.i2asolutions.athelo.websocket.WebSocketManager
import javax.inject.Inject

class LoadMessagesUseCase @Inject constructor(
    private val messageRepository: ChatRepository,
    private val webSocketManager: WebSocketManager
) {

    suspend operator fun invoke(
        nextUrl: String?,
        userId: List<Int>
    ): PageResponse<PrivateConversation> {
        var conversationResult = messageRepository.loadPrivateConversations(nextUrl, userId)
        val missingConversations =
            userId.filter { id -> conversationResult.results.firstOrNull { it.user_profile?.id == id } == null }
        missingConversations.map { messageRepository.createPrivateConversations(it.toString()) }
        conversationResult = messageRepository.loadPrivateConversations(nextUrl, userId)
        val chatsIds = conversationResult.results.mapNotNull { it.chatRoomIdentifier }
        if (chatsIds.isEmpty()) return PageResponse(emptyList(), null)
        val unreadMessage = webSocketManager.getUnreadMessagesCount(chatsIds)
        val chatsLastMessage = webSocketManager.getLastChatRoomMessage(chatsIds)
        return conversationResult.toPageResponse { item ->
            item.toPrivateConversation(unreadMessage.payload.firstOrNull { it.chatId == item.chatRoomIdentifier }?.count
                ?: 0,
                chatsLastMessage.payload.firstOrNull { it.chatId == item.chatRoomIdentifier })
        }
    }
}