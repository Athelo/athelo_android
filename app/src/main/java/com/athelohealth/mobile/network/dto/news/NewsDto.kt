package com.athelohealth.mobile.network.dto.news


import com.athelohealth.mobile.extensions.displayAsDifferentDateFormat
import com.athelohealth.mobile.network.dto.base.ImageDto
import com.athelohealth.mobile.network.dto.member.UserDto
import com.athelohealth.mobile.presentation.model.news.News
import com.athelohealth.mobile.utils.consts.DATE_FORMAT_FULL
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewsDto(
    @SerialName("categories")
    val categories: List<CategoryDto>? = null,
    @SerialName("comment_count")
    val commentCount: Int? = null,
    @SerialName("content")
    val content: String? = null,
    @SerialName("content_url")
    val contentUrl: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("is_favourite")
    val isFavourite: Boolean? = null,
    @SerialName("owner")
    val owner: UserDto? = null,
    @SerialName("photo")
    val photo: ImageDto? = null,
    @SerialName("recent_comments")
    val recentComments: List<CommentDto>? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null,
//    @SerialName("video_url")
//    val videoUrl: AnyDto? = null
) {
    fun toNews(): News {
        return News(
            id = id ?: -1,
            isFavourite = isFavourite == true,
            name = title ?: "",
            description = content ?: "",
            image = photo?.toImage(),
            categories = categories?.mapNotNull { it.name } ?: emptyList(),
            createDate = createdAt.displayAsDifferentDateFormat(DATE_FORMAT_FULL),
            articleLink = contentUrl
        )
    }
}