package com.athelohealth.mobile.network.dto.heartRate

import com.athelohealth.mobile.extensions.toDate
import com.athelohealth.mobile.presentation.model.heartRate.HeartRateEntry
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