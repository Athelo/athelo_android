package com.athelohealth.mobile.network.dto.heartRate

import com.athelohealth.mobile.extensions.toDate
import com.athelohealth.mobile.presentation.model.heartRate.HeartRateEntry
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
class HeartRateEntryDto(
    @SerialName("date") val date: String,
    @SerialName("value") val value: Float,
) {
    fun toHeartRateEntry() =
        HeartRateEntry(date.toDate() ?: Date(), value.toInt())
}