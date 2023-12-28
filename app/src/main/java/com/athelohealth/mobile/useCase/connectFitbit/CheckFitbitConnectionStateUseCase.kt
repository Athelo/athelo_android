package com.athelohealth.mobile.useCase.connectFitbit

import com.athelohealth.mobile.utils.fitbit.FitbitConnectionHelper
import javax.inject.Inject

class CheckFitbitConnectionStateUseCase @Inject constructor(private val fitbitConnectionHelper: FitbitConnectionHelper) {
    suspend operator fun invoke() {
        fitbitConnectionHelper.checkState()
    }
}