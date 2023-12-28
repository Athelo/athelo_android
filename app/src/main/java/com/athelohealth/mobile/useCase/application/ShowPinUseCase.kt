package com.athelohealth.mobile.useCase.application

import com.athelohealth.mobile.utils.PreferenceHelper
import javax.inject.Inject

class ShowPinUseCase @Inject constructor(val preferenceHelper: PreferenceHelper) {
    operator fun invoke(): Boolean {
        return !preferenceHelper.hidePin
    }
}