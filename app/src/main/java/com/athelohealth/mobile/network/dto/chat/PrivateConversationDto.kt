package com.athelohealth.mobile.network.dto.chat


import com.athelohealth.mobile.network.dto.member.UserDto
import com.athelohealth.mobile.presentation.model.chat.ChatLastMessageInfo
import com.athelohealth.mobile.presentation.model.chat.Conversation
import com.athelohealth.mobile.presentation.model.chat.PrivateConversation
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PrivateConversationDto(
    @SerialName("chat_room_identifier")
    val chatRoomIdentifier: String? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("user_profile")
    val user_profile: UserDto? = null,
) {
    fun toPrivateConversation(
        unreadCount: Int,
        lastMessageInfo: ChatLastMessageInfo?
    ): PrivateConversation? {
        return if (user_profile == null) {
            null
        } else
            PrivateConversation(
                chatRoomId = chatRoomIdentifier ?: "",
                conversationId = id ?: 0,
                user = user_profile.toSimpleUser(),
                unreadMessages = unreadCount,
                lastMessage = lastMessageInfo?.lastMessage ?: "",
                lastMessageDate = lastMessageInfo?.date
            )
    }

    fun toConversation(): Conversation? {
        if (user_profile == null) return null
        return Conversation(
            conversationId = id ?: 0,
            chatRoomId = chatRoomIdentifier ?: "",
            users = listOf(user_profile.toSimpleUser()),
            name = user_profile.displayName ?: "",
            myConversation = true,
            participantsCount = 1,
        )
    }

}