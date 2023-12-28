package com.athelohealth.mobile.network.dto.sleep

import com.athelohealth.mobile.extensions.toDate
import com.athelohealth.mobile.presentation.model.sleep.SleepEntry
import com.athelohealth.mobile.presentation.model.sleep.SleepLevel
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