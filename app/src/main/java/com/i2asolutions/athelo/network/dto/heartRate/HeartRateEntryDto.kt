package com.i2asolutions.athelo.network.dto.heartRate

import com.i2asolutions.athelo.extensions.toDate
import com.i2asolutions.athelo.presentation.model.heartRate.HeartRateEntry
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