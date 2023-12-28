package com.athelohealth.mobile.presentation.ui.patient.device

import com.athelohealth.mobile.extensions.displaySecsAsTime

data class DeviceConfig(
    val idealSleepSecs: Int? = null,
    val idealSleepArticleId: Int? = null,
    val idealSleepText: String? = null,
) {
    val idealSleepSecsFormatted: String = idealSleepSecs?.displaySecsAsTime() ?: ""
}