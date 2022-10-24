package com.i2asolutions.athelo.network.dto.heartRate

import com.i2asolutions.athelo.extensions.toDate
import com.i2asolutions.athelo.presentation.model.heartRate.HeartRateEntry
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
class HeartRateRecordDto(
    @SerialName("collected_at_date") val date: String,
    @SerialName("collected_at_time") val time: String,
    @SerialName("value") val value: Float,
) {
    fun toHeartRateEntry() =
        HeartRateEntry("${date}T${time}".toDate() ?: Date(), value.toInt())
}