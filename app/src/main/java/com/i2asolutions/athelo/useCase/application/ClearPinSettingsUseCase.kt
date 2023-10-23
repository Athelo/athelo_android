package com.i2asolutions.athelo.useCase.application

import com.i2asolutions.athelo.utils.PreferenceHelper
import javax.inject.Inject

class ClearPinSettingsUseCase @Inject constructor(val preferenceHelper: PreferenceHelper) {
    operator fun invoke() {
        preferenceHelper.hidePin = false
    }
}