package com.i2asolutions.athelo.useCase.connectFitbit

import com.i2asolutions.athelo.utils.fitbit.FitbitConnectionHelper
import javax.inject.Inject

class CheckFitbitConnectionStateUseCase @Inject constructor(private val fitbitConnectionHelper: FitbitConnectionHelper) {
    suspend operator fun invoke() {
        fitbitConnectionHelper.checkState()
    }
}