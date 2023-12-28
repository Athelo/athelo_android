package com.athelohealth.mobile.network.dto.device

import com.athelohealth.mobile.presentation.ui.patient.device.DeviceConfig
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class DeviceConfigDto(
    @SerialName("application_settings") val applicationSettings: ApplicationSettingsDto? = null
) {
    fun toDeviceConfig() = DeviceConfig(
        idealSleepSecs = applicationSettings?.details?.sleepSettings?.idealSleepSecs,
        idealSleepArticleId = applicationSettings?.details?.sleepSettings?.articleId,
        idealSleepText = applicationSettings?.details?.sleepSettings?.idealSleepText,
    )
}
