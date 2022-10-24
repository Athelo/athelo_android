package com.i2asolutions.athelo.network.dto.health


import com.i2asolutions.athelo.presentation.model.health.Symptom
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserSymptomDto(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("note")
    val note: String? = null,
    @SerialName("occurrence_date")
    val occurrenceDate: String? = null,
    @SerialName("symptom")
    val symptom: SymptomDto? = null
) {
    fun toSymptom(): Symptom {
        return Symptom(
            id = id ?: -1,
            symptom?.name ?: "",
            comment = note ?: "",
            description = symptom?.description,
            icon = symptom?.icon?.toImage(),
            symptomId = symptom?.id ?: -1
        )
    }
}