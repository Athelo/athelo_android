package com.i2asolutions.athelo.network.dto.device

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ApplicationSettingsDto(
    @SerialName("details") val details: ApplicationSettingsDetailsDto? = null
)
