package com.i2asolutions.athelo.network.dto.caregiver

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class CaregiverRelationshipDto(
    @SerialName("caregiver") val caregiver: CaregiverDto? = null,
    @SerialName("id") val id: Int? = null,
    @SerialName("patient") val patient: PatientDto? = null,
    @SerialName("relation_label") val relations: String? = null,
)