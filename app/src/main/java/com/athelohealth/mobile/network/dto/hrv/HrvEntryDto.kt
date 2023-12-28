package com.athelohealth.mobile.network.dto.hrv

import com.athelohealth.mobile.extensions.toDate
import com.athelohealth.mobile.presentation.model.hrv.HrvEntry
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
class HrvEntryDto(
    @SerialName("date") val date: String,
    @SerialName("high_frequency") val highFrequency: Float,
    @SerialName("low_frequency") val lowFrequency: Float,
    @SerialName("rmssd") val rmssd: Float,
    @SerialName("coverage") val coverage: Float,
) {
    fun toHrvEntry() =
        HrvEntry(date.toDate() ?: Date(), rmssd.toInt())
}