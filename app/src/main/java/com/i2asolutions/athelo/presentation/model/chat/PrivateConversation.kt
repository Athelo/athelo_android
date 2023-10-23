package com.i2asolutions.athelo.presentation.model.chat

import java.util.*

data class PrivateConversation(
    val conversationId: Int,
    val chatRoomId: String,
    val user: SimpleUser,
    val unreadMessages: Int,
    val lastMessage: String,
    val lastMessageDate: Date?,
)
