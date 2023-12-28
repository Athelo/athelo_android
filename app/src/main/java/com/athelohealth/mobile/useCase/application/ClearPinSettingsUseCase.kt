package com.athelohealth.mobile.useCase.application

import com.athelohealth.mobile.utils.PreferenceHelper
import javax.inject.Inject

class ClearPinSettingsUseCase @Inject constructor(val preferenceHelper: PreferenceHelper) {
    operator fun invoke() {
        preferenceHelper.hidePin = false
    }
}