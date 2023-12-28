package com.athelohealth.mobile.network.dto.device

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ApplicationSettingsDto(
    @SerialName("details") val details: ApplicationSettingsDetailsDto? = null
)
