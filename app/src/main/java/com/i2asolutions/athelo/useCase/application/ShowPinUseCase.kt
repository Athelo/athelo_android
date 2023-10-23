package com.i2asolutions.athelo.useCase.application

import com.i2asolutions.athelo.utils.PreferenceHelper
import javax.inject.Inject

class ShowPinUseCase @Inject constructor(val preferenceHelper: PreferenceHelper) {
    operator fun invoke(): Boolean {
        return !preferenceHelper.hidePin
    }
}