package com.athelohealth.mobile.network.dto.news


import com.athelohealth.mobile.network.dto.base.ImageDto
import com.athelohealth.mobile.network.dto.member.UserDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentDto(
    @SerialName("content")
    val content: String? = "",
    @SerialName("created_at")
    val createdAt: String? = "",
    @SerialName("id")
    val id: Int? = null,
    @SerialName("owner")
    val owner: UserDto?,
    @SerialName("photos")
    val photos: List<ImageDto>? = null,
    @SerialName("updated_at")
    val updatedAt: String? = ""
)