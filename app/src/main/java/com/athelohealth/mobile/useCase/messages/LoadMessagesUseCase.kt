package com.athelohealth.mobile.useCase.messages

import com.athelohealth.mobile.network.repository.chat.ChatRepository
import com.athelohealth.mobile.presentation.model.base.PageResponse
import com.athelohealth.mobile.presentation.model.chat.PrivateConversation
import com.athelohealth.mobile.websocket.WebSocketManager
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