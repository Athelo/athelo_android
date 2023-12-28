package com.athelohealth.mobile.websocket

import com.athelohealth.mobile.presentation.model.chat.ConversationInfo
import com.athelohealth.mobile.websocket.constant.*
import com.athelohealth.mobile.websocket.model.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject

object WebSocketParser {

    fun parseMessage(text: String, parser: Json): ConversationInfo? {
        val type = runCatching {
            val testJson = JSONObject(text)
            testJson.getString("type")
        }.getOrNull()
        if (type == null || type.isBlank()) return null
        return runCatching {
            when (type) {
                ROUTABLE -> {
                    parser.decodeFromString<RoutableResponseDto>(text)
                        .toConversationMessage(parser)
                }
                SYSTEM_ROUTABLE -> {
                    parser.decodeFromString<SystemRoutableResponseDto>(text)
                        .toConversationMessage()
                }
                GET_HISTORY -> {
                    parser.decodeFromString<GetHistoryResponseDto>(text)
                        .toConversationMessageList(parser)
                }
                SET_LAST_MESSAGE_READ -> {
                    parser.decodeFromString<SetLastMessageReadResultDto>(text)
                        .toConversationMessage(parser)
                }
                GET_LAST_MESSAGES_READ -> {
                    parser.decodeFromString<GetLastMessageReadResultDto>(text)
                        .toConversationMessageList(parser)
                }
                GET_LAST_CHAT_ROOM_MESSAGE -> {
                    parser.decodeFromString<GetLastChatRoomMessageResultDto>(text)
                        .toConversationLastMessage()
                }
                GET_UNREAD_MESSAGES_COUNT -> {
                    parser.decodeFromString<GetUnreadMessagesCountResultDto>(text)
                        .toConversationUnreadMessageCount()
                }
                ERROR -> {
                    parser.decodeFromString<ErrorMessageDto>(text)
                        .toConversationError()
                }
                else -> {
                    null
                }
            }
        }.onFailure { it.printStackTrace() }.getOrNull()
    }
}