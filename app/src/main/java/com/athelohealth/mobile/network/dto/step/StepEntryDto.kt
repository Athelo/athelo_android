package com.athelohealth.mobile.network.dto.step

import com.athelohealth.mobile.extensions.toDate
import com.athelohealth.mobile.presentation.model.step.StepEntry
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