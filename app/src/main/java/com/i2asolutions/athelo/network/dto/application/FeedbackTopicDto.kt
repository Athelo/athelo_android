package com.i2asolutions.athelo.network.dto.application


import com.i2asolutions.athelo.presentation.model.enums.EnumItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeedbackTopicDto(
    @SerialName("application")
    val application: Int? = null,
    @SerialName("category")
    val category: Int? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String? = null
) {
    fun toEnumItem(): EnumItem? {
        return if (id == null || name == null) null else EnumItem(id.toString(), name)
    }
}