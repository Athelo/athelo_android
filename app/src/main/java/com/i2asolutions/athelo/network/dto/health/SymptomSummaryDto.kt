package com.i2asolutions.athelo.network.dto.health

import com.i2asolutions.athelo.presentation.model.health.SymptomSummary
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SymptomSummaryDto(
    @SerialName("symptom") val symptomDto: SymptomDto? = null,
    @SerialName("occurrences_count") val count: Int? = null,
) {
    fun toSymptomSummary(): SymptomSummary? {

        return symptomDto?.let { symptom ->
            SymptomSummary(
                symptom = symptom.toSymptom(),
                count = count ?: 0
            )
        }
    }

}