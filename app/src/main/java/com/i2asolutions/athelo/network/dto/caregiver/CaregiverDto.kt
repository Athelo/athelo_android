package com.i2asolutions.athelo.network.dto.caregiver

import com.i2asolutions.athelo.network.dto.base.ImageDto
import com.i2asolutions.athelo.presentation.model.caregiver.Caregiver
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class CaregiverDto(
    @SerialName("id") val id: Int? = null,
    @SerialName("display_name") val displayName: String? = null,
    @SerialName("photo") val photo: ImageDto? = null,
) {
    fun toCaregiver(relation: String, relationId: String): Caregiver {
        return Caregiver(
            photo = photo?.toImage(),
            displayName = displayName ?: "",
            id = id ?: -1,
            relation = relation,
            relationId= relationId
        )
    }
}