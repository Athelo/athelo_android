package com.athelohealth.mobile.network.dto.device

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ApplicationSettingsDetailsDto(
    @SerialName("sleep_settings") val sleepSettings: SleepSettingsDto? = null
)
