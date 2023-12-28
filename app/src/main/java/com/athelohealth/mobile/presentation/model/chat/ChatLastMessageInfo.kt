package com.athelohealth.mobile.presentation.model.chat

import java.util.*

data class ChatLastMessageInfo(
    val chatId: String,
    val lastMessage: String,
    val messageId: String,
    val hasUnreadMessages: Boolean,
    val date: Date,
)