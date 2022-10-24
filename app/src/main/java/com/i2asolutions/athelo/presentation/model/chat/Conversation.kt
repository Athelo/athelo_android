package com.i2asolutions.athelo.presentation.model.chat

data class Conversation(
    val conversationId: Int,
    val chatRoomId: String,
    val users: List<SimpleUser>,
    val name: String,
    val myConversation: Boolean,
    val participantsCount: Int,
)