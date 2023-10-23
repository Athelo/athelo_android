package com.i2asolutions.athelo.network.dto.caregiver

import com.i2asolutions.athelo.network.dto.base.ImageDto
import com.i2asolutions.athelo.presentation.model.patients.Patient
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class PatientDto(
    @SerialName("id") val id: Int? = null,
    @SerialName("display_name") val displayName: String? = null,
    @SerialName("photo") val photo: ImageDto? = null,
) {
    fun toPatient(): Patient {
        return Patient(
            userId = id.toString(),
            name = displayName ?: "",
            relation = "",
            image = photo?.toImage(),
        )
    }
}