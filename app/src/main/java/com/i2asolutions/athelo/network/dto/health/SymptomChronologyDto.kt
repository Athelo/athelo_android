package com.i2asolutions.athelo.network.dto.health

import com.i2asolutions.athelo.extensions.toDate
import com.i2asolutions.athelo.presentation.model.health.SymptomChronology
import com.i2asolutions.athelo.utils.consts.DATE_FORMAT_SIMPLE_DAY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class SymptomChronologyDto(
    @SerialName("symptoms") val symptoms: List<UserSymptomDto>? = null,
    @SerialName("occurrence_date") val occurrenceDate: String? = null,
    @SerialName("feelings") val feelings: List<WellbeingDto>? = null
) {
    fun toSymptomChronology(): SymptomChronology {
        return SymptomChronology(
            symptoms = symptoms?.map { it.toSymptom() } ?: emptyList(),
            feeling = feelings?.asSequence()?.sortedByDescending { it.id }?.firstOrNull()
                ?.toWellbeing()?.toFeelings(),
            date = occurrenceDate.toDate(DATE_FORMAT_SIMPLE_DAY) ?: Date()
        )
    }
}
