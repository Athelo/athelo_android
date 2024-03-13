package com.athelohealth.mobile.network.dto.news


import com.athelohealth.mobile.presentation.model.news.Category
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    @SerialName("id") val id: Int? = null,
    @SerialName("name") val name: String? = null
) {
    fun toCategory(): Category {
        return Category(id.toString(), name ?: "")
    }
}