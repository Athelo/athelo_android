package com.athelohealth.mobile.network.dto.chat


import com.athelohealth.mobile.network.dto.member.UserDto
import com.athelohealth.mobile.presentation.model.chat.Conversation
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConversationDto(
    @SerialName("chat_room_identifier")
    val chatRoomIdentifier: String? = null,
    @SerialName("chat_room_type")
    val chatRoomType: Int? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("is_public")
    val isPublic: Boolean? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("owner")
    val owner: UserDto? = null,
    @SerialName("user_profiles")
    val userProfiles: List<UserDto>? = null,
    @SerialName("user_profiles_count")
    val userProfilesCount: Int? = null,
    @SerialName("belong_to")
    val belongTo: Boolean? = null
) {
    fun toConversation(): Conversation? {
        return id?.let { id ->
            Conversation(
                conversationId = id,
                chatRoomId = chatRoomIdentifier ?: "",
                users = userProfiles?.map { it.toSimpleUser() } ?: emptyList(),
                name = name ?: "",
                myConversation = belongTo ?: false,
                participantsCount = userProfilesCount ?: 0
            )
        }
    }
}