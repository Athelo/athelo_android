package com.athelohealth.mobile.useCase.common

import com.athelohealth.mobile.utils.PreferenceHelper
import javax.inject.Inject

class CheckTutorialStateUseCase @Inject constructor(private val preferenceHelper: PreferenceHelper) {

    suspend operator fun invoke(): Boolean {
        return preferenceHelper.showTutorial.also { preferenceHelper.showTutorial = false }
    }
}