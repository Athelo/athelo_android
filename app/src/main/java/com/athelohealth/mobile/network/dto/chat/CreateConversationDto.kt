package com.athelohealth.mobile.network.dto.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateConversationDto(
    @SerialName("chat_room_identifier") val chatRoomIdentifier: String,
    @SerialName("user_profile_id") val userProfileId: Int
)
