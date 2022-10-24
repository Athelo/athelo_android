package com.i2asolutions.athelo.network.dto.step

import com.i2asolutions.athelo.extensions.toDate
import com.i2asolutions.athelo.presentation.model.step.StepEntry
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
class StepEntryDto(
    @SerialName("date") val date: String,
    @SerialName("value") val value: Float,
) {
    fun toStepEntry() =
        StepEntry(date.toDate() ?: Date(), value.toInt())
}