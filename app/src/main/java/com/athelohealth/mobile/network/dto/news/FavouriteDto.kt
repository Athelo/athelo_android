package com.athelohealth.mobile.network.dto.news

import com.athelohealth.mobile.presentation.model.news.FavouriteContent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FavouriteDto(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("external_content_id")
    val externalContentId: String? = null,
    @SerialName("user_profile_id")
    val userProfileId: Int? = null
) {
    fun toFavouriteContent(): FavouriteContent {
        return FavouriteContent(id = id, externalContentId = externalContentId, userProfileId = userProfileId)
    }
}