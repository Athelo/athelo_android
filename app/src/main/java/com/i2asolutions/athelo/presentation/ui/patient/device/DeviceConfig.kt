package com.i2asolutions.athelo.presentation.ui.patient.device

import com.i2asolutions.athelo.extensions.displaySecsAsTime

data class DeviceConfig(
    val idealSleepSecs: Int? = null,
    val idealSleepArticleId: Int? = null,
    val idealSleepText: String? = null,
) {
    val idealSleepSecsFormatted: String = idealSleepSecs?.displaySecsAsTime() ?: ""
}