package com.i2asolutions.athelo.network.dto.health


import com.i2asolutions.athelo.network.dto.base.ImageDto
import com.i2asolutions.athelo.presentation.model.health.Symptom
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SymptomDto(
    @SerialName("description")
    val description: String? = null,
    @SerialName("icon")
    val icon: ImageDto? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("is_public")
    val isPublic: Boolean? = null,
    @SerialName("name")
    val name: String? = null
) {
    fun toSymptom(): Symptom {
        return Symptom(
            id = id ?: -1,
            name = name ?: "",
            description = description,
            icon = icon?.toImage(),
            symptomId = id ?: -1
        )
    }
}