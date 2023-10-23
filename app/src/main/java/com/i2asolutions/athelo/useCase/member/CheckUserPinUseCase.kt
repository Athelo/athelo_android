package com.i2asolutions.athelo.useCase.member

import com.i2asolutions.athelo.utils.PreferenceHelper
import javax.inject.Inject

class CheckUserPinUseCase @Inject constructor(val preferenceHelper: PreferenceHelper) {
    operator fun invoke(pin: String): Boolean {
        val validation = pin == "1107"
        if (validation) {
            preferenceHelper.hidePin = true
        }
        return validation
    }
}