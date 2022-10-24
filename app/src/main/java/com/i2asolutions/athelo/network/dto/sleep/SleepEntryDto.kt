package com.i2asolutions.athelo.network.dto.sleep

import com.i2asolutions.athelo.extensions.toDate
import com.i2asolutions.athelo.presentation.model.sleep.SleepEntry
import com.i2asolutions.athelo.presentation.model.sleep.SleepLevel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
class SleepEntryDto(
    @SerialName("date") val date: String,
    @SerialName("duration") val duration: Float,
    @SerialName("level") val level: String
) {
    fun toSleepEntry() =
        SleepEntry(date.toDate() ?: Date(), duration.toInt(), SleepLevel.fromWsName(level))
}