package com.i2asolutions.athelo.websocket.model

import com.i2asolutions.athelo.presentation.model.chat.UnreadMessageCount
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class WebSocketMessageCountPayloadDto(
    @SerialName("chat_room_identifier") val chatRoomIdentifier: String,
    @SerialName("unread_messages_count") val unreadMessagesCount: Int,
) {
    fun toUnreadMessageCount(): UnreadMessageCount {
        return UnreadMessageCount(chatRoomIdentifier, unreadMessagesCount)
    }
}