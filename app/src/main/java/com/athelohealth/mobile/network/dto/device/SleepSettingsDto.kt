package com.athelohealth.mobile.network.dto.device

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class SleepSettingsDto(
    @SerialName("article_id") val articleId: Int? = null,
    @SerialName("ideal_sleep_secs") val idealSleepSecs: Int? = null,
    @SerialName("ideal_sleep_text") val idealSleepText: String? = null,
)
