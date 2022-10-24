package com.i2asolutions.athelo.useCase.common

import com.i2asolutions.athelo.utils.PreferenceHelper
import javax.inject.Inject

class CheckTutorialStateUseCase @Inject constructor(private val preferenceHelper: PreferenceHelper) {

    suspend operator fun invoke(): Boolean {
        return preferenceHelper.showTutorial.also { preferenceHelper.showTutorial = false }
    }
}