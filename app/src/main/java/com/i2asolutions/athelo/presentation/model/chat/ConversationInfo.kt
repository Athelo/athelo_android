package com.i2asolutions.athelo.presentation.model.chat

import com.i2asolutions.athelo.extensions.timestampTo
import com.i2asolutions.athelo.extensions.toDateTimeFormat
import com.i2asolutions.athelo.presentation.model.member.User
import java.text.SimpleDateFormat
import java.util.*

sealed class ConversationInfo(open val type: String) {

    /**
     * Class for type: ROUTABLE, SET_LAST_MESSAGE_READ
     */
    data class ConversationMessage(
        override val type: String,
        val messageId: String,
        val isSystemMessage: Boolean,
        val chatId: String,
        val userId: String,
        val message: String,
        val user: User? = null,
        val date: Date
    ) : ConversationInfo(type) {

        val dateTimeToDisplay: String
            get() {
                val sdf = SimpleDateFormat(timeFormat.format, Locale.US)
                return sdf.format(date)
            }

        private val timeFormat
            get() = Date().timestampTo(date).toDateTimeFormat()

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ConversationMessage

            if (messageId != other.messageId) return false

            return true
        }

        override fun hashCode(): Int {
            return messageId.hashCode()
        }


    }

    /**
     * Class for type: GET_HISTORY, GET_LAST_MESSAGES_READ
     */
    data class ConversationMessageList(
        override val type: String,
        val payload: List<ConversationMessage>
    ) : ConversationInfo(type)

    /**
     * Class for type: GET_UNREAD_MESSAGES_COUNT
     */
    data class ConversationUnreadMessageCount(
        override val type: String,
        val payload: List<UnreadMessageCount>
    ) : ConversationInfo(type)

    /**
     * Class for type: GET_LAST_CHAT_ROOM_MESSAGE
     */
    data class ConversationLastMessage(
        override val type: String,
        val payload: List<ChatLastMessageInfo>
    ) : ConversationInfo(type)

    /**
     * Class for type: ERROR
     */
    data class ConversationError(
        override val type: String,
        val message: String
    ) : ConversationInfo(type)
}