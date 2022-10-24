package com.i2asolutions.athelo.network.dto.device

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ApplicationSettingsDetailsDto(
    @SerialName("sleep_settings") val sleepSettings: SleepSettingsDto? = null
)
