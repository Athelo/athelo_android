package com.i2asolutions.athelo.websocket.model

import com.i2asolutions.athelo.extensions.nanosecondsToDate
import com.i2asolutions.athelo.extensions.toNanoseconds
import com.i2asolutions.athelo.presentation.model.chat.ChatLastMessageInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
class WebSocketLastChatMessageDto(
    @SerialName("chat_room_identifier") val chatRoomIdentifier: String,
    @SerialName("last_message_text") val lastMessageText: String? = null,
    @SerialName("message_timestamp_identifier") val messageTimestampIdentifier: String? = null,
    @SerialName("has_unread_messages") val hasUnreadMessages: Boolean? = null,
) {
    fun toChatLastMessageInfo(): ChatLastMessageInfo {
        val messageId = messageTimestampIdentifier
            ?: Calendar.getInstance(TimeZone.getTimeZone("UTC")).time.toNanoseconds().toString()
        return ChatLastMessageInfo(
            chatRoomIdentifier,
            lastMessageText ?: "",
            messageId,
            hasUnreadMessages ?: false,
            messageId.nanosecondsToDate()
        )
    }
}