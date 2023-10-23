package com.i2asolutions.athelo.network.dto.news


import com.i2asolutions.athelo.presentation.model.news.Category
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    @SerialName("id") val id: Int? = null,
    @SerialName("name") val name: String? = null
) {
    fun toCategory(): Category {
        return Category(id ?: -1, name ?: "")
    }
}