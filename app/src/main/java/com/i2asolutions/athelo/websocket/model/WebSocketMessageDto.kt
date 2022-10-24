package com.i2asolutions.athelo.websocket.model

import com.i2asolutions.athelo.extensions.nanosecondsToDate
import com.i2asolutions.athelo.network.dto.member.UserDto
import com.i2asolutions.athelo.presentation.model.chat.ConversationInfo
import com.i2asolutions.athelo.websocket.constant.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Serializable
sealed class WebSocketMessageDto(@WebSocketType @SerialName("type") val type: String? = null)

/**
 * ROUTABLE - a message that user want to send to all other people in a chat room
 */
@Serializable
class RoutableRequestDto(
    @SerialName("chat_room_identifier") val chatRoomIdentifier: String,
    @SerialName("message") val message: String
) : WebSocketMessageDto(ROUTABLE)

/**
 * HISTORY - chat room history. Returns {limit} messages that occurred BEFORE given point in time. Returned messages
 * are sorted from newer to older.
 */
@Serializable
class GetHistoryRequestDto(
    @SerialName("chat_room_identifier") val chatRoomIdentifier: String,
    @SerialName("from_message_timestamp_identifier") val fromMessageTimestampIdentifier: Long = 0,
    @SerialName("limit") val limit: Int = 100
) : WebSocketMessageDto(GET_HISTORY)

/**
 * SET_LAST_MESSAGE_READ - marks a chat room message as read. Server will automatically notify all online users
 * who read which message.
 */
@Serializable
class SetLastMessageReadRequestDto(
    @SerialName("chat_room_identifier") val chatRoomIdentifier: String,
    @SerialName("message_timestamp_identifier") val messageTimestampIdentifier: Long
) : WebSocketMessageDto(SET_LAST_MESSAGE_READ)

/**
 * GET_LAST_MESSAGES_READ - get information about last message that all users has read in chat room. Use this ONLY
 * when user enters the chat room on a new session.
 */
@Serializable
class GetLastMessageReadRequestDto(
    @SerialName("chat_room_identifier") val chatRoomIdentifier: String
) : WebSocketMessageDto(GET_LAST_MESSAGES_READ)

/**
 * GET_LAST_CHAT_ROOM_MESSAGE - specify a list of chat room identifiers and get last 1 last message published in each
 * of those chat rooms in response.
 */
@Serializable
class GetLastChatRoomMessageRequestDto(
    @SerialName("chat_room_identifiers") val chatRoomIdentifiers: Array<String>
) : WebSocketMessageDto(GET_LAST_CHAT_ROOM_MESSAGE)

/**
 * GET_UNREAD_MESSAGES_COUNT - specify a list of chat room indentifiers (max 10) and get a number of unread messages
 * the calling user has in each of those chat rooms. The operation is quite heavy on the server side and is meant to be
 * used in paginated views only, hence the number of chat room identiers you can send is limited to 10. Another
 * limitation is that if user has more than 100 unread messages in given chatroom, the response will still return a
 * count of 100. The chat client should display this as "99+ unread messages".
 */
@Serializable
class GetUnreadMessagesCountRequestDto(
    @SerialName("chat_room_identifiers") val chatRoomIdentifiers: Array<String>
) : WebSocketMessageDto(GET_UNREAD_MESSAGES_COUNT)

/**
 * ROUTABLE - you receive that message when someone writes a message in a chat room. Note that you will get this message
 * also when you are the person writing a message (message "echoes" on the server)
 * chat_room_identifier - chat room in which the message was written
 * message_timestamp_identifier - message timestamp in form of unix epoch time, in nanoseconds
 * app_user_identifier - identifier of the use who wrote the message
 * custom_data - custom data associated with the user who wrote the message
 * message - the message content
 */
@Serializable
class RoutableResponseDto(
    @SerialName("message_timestamp_identifier") val messageTimestampIdentifier: String,
    @SerialName("chat_room_identifier") val chatRoomIdentifier: String,
    @SerialName("is_system_message") val isSystemMessage: Boolean? = null,
    @SerialName("app_user_identifier") val appUserIdentifier: String? = null,
    @SerialName("message") val message: String? = null,
    @SerialName("custom_data") val customData: String? = null
) : WebSocketMessageDto(ROUTABLE) {

    fun toConversationMessage(parser: Json): ConversationInfo.ConversationMessage {
        val user = customData?.let { customData ->
            print(customData)
            runCatching {
                parser.decodeFromString<UserDto>(customData).also { print(it) }.toUser()
                    .also { print(it) }
            }.onFailure { it.printStackTrace() }.getOrNull()
        }
        return ConversationInfo.ConversationMessage(
            ROUTABLE,
            messageTimestampIdentifier,
            isSystemMessage ?: false,
            chatRoomIdentifier,
            appUserIdentifier ?: "system",
            message ?: "",
            user,
            messageTimestampIdentifier.nanosecondsToDate()
        )
    }
}

/**
 * SYSTEM_ROUTABLE - you receive that message when someone writes a message in a chat room. Note that you will get this message
 * also when you are the person writing a message (message "echoes" on the server)
 * chat_room_identifier - chat room in which the message was written
 * message_timestamp_identifier - message timestamp in form of unix epoch time, in nanoseconds
 * app_user_identifier - identifier of the use who wrote the message
 * custom_data - custom data associated with the user who wrote the message
 * message - the message content
 */
@Serializable
class SystemRoutableResponseDto(
    @SerialName("message_timestamp_identifier") val messageTimestampIdentifier: String,
    @SerialName("chat_room_identifier") val chatRoomIdentifier: String,
    @SerialName("message") val message: String? = null,
) : WebSocketMessageDto(ROUTABLE) {

    fun toConversationMessage(): ConversationInfo.ConversationMessage {
        return ConversationInfo.ConversationMessage(
            ROUTABLE,
            messageTimestampIdentifier,
            true,
            chatRoomIdentifier,
            "system",
            message ?: "",
            null,
            messageTimestampIdentifier.nanosecondsToDate()
        )
    }
}

/**
 * HISTORY - List with a history of messages - you receive this message as a response to GET_HISTORY message sent to the socket.
 */
@Serializable
class GetHistoryResponseDto(
    @SerialName("payload") val payload: Array<RoutableResponseDto>,
) : WebSocketMessageDto(GET_HISTORY) {

    fun toConversationMessageList(parser: Json): ConversationInfo.ConversationMessageList {
        return ConversationInfo.ConversationMessageList(
            GET_HISTORY,
            payload.map { it.toConversationMessage(parser) }
        )
    }
}

/**
 * SET_LAST_MESSAGE_READ - A notification from the server indicating that a specific user has marked specific message as read
 * chat_room_identifier - chat room in which the message was written
 * message_timestamp_identifier - timestamp of LAST message marked as read, in form of unix epoch time,
 * in nanoseconds
 * app_user_identifier - identifier of the use who marked the message as read
 * custom_data - custom data associated with the user who marked the message as read
 */
@Serializable
class SetLastMessageReadResultDto(
    @SerialName("message_timestamp_identifier") val messageTimestampIdentifier: Long,
    @SerialName("chat_room_identifier") val chatRoomIdentifier: String,
    @SerialName("app_user_identifier") val appUserIdentifier: String,
    @SerialName("custom_data") val customData: String
) : WebSocketMessageDto(SET_LAST_MESSAGE_READ) {

    fun toConversationMessage(parser: Json): ConversationInfo.ConversationMessage {
        val user = runCatching {
            parser.decodeFromString<UserDto>(customData).toUser()
        }.getOrNull()
        return ConversationInfo.ConversationMessage(
            SET_LAST_MESSAGE_READ,
            messageTimestampIdentifier.toString(),
            false,
            chatRoomIdentifier,
            appUserIdentifier,
            "",
            user,
            messageTimestampIdentifier.nanosecondsToDate(),
        )
    }
}

/**
 * GET_LAST_MESSAGES_READ - List with last messages read by users in a chatroom. You receive this message as a response to GET_LAST_MESSAGES_READ
 * sent to the server. It includes last message read by each user in the chat room, so for example, if there are 5
 * users in the chatroom, the list will have 5 elements, one for each user.
 * chat_room_identifier - chat room in which the message was written
 * message_timestamp_identifier - timestamp of LAST message marked as read, in form of unix epoch time,
 * in nanoseconds
 * app_user_identifier - identifier of the use who marked the message as read
 * custom_data - custom data associated with the user who marked the message as read
 */
@Serializable
class GetLastMessageReadResultDto(
    @SerialName("payload") val payload: Array<RoutableResponseDto>,
) : WebSocketMessageDto(GET_LAST_MESSAGES_READ) {

    fun toConversationMessageList(parser: Json): ConversationInfo.ConversationMessageList {
        return ConversationInfo.ConversationMessageList(
            GET_LAST_MESSAGES_READ,
            payload.map { it.toConversationMessage(parser) }
        )
    }
}

/**
 * GET_LAST_CHAT_ROOM_MESSAGE - specify a list of chat room identifiers and get last 1 last message published in each
 * of those chat rooms in response.
 * The payload object will list the newest chat message from all chat rooms sent in the request. The 'has_unread_messages'
 * flag is true if calling user has at least one unread message in given chat room, otherwise it's false.
 */
@Serializable
class GetLastChatRoomMessageResultDto(
    @SerialName("payload") val payload: Array<WebSocketLastChatMessageDto>,
) : WebSocketMessageDto(GET_LAST_CHAT_ROOM_MESSAGE) {

    fun toConversationLastMessage(): ConversationInfo.ConversationLastMessage {
        return ConversationInfo.ConversationLastMessage(
            GET_LAST_CHAT_ROOM_MESSAGE,
            payload.map { it.toChatLastMessageInfo() }
        )
    }
}

/**
 * A response to GET_UNREAD_MESSAGES_COUNT request
 * The payload object will list unread messages counts for all chat rooms sent in the request.
 */
@Serializable
class GetUnreadMessagesCountResultDto(
    @SerialName("payload") val payload: Array<WebSocketMessageCountPayloadDto>
) : WebSocketMessageDto(GET_UNREAD_MESSAGES_COUNT) {

    fun toConversationUnreadMessageCount(): ConversationInfo.ConversationUnreadMessageCount {
        return ConversationInfo.ConversationUnreadMessageCount(
            GET_UNREAD_MESSAGES_COUNT,
            payload.map { it.toUnreadMessageCount() }
        )
    }
}

@Serializable
class ErrorMessageDto(@SerialName("exception") val exception: WebSocketErrorDto? = null) :
    WebSocketMessageDto(ERROR) {

    fun toConversationError(): ConversationInfo.ConversationError {
        return ConversationInfo.ConversationError(ERROR, exception?.message ?: "")
    }
}