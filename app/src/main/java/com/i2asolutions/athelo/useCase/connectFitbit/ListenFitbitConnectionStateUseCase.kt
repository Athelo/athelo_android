package com.i2asolutions.athelo.useCase.connectFitbit

import com.i2asolutions.athelo.utils.fitbit.FitbitConnectionHelper
import com.i2asolutions.athelo.utils.fitbit.FitbitState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ListenFitbitConnectionStateUseCase @Inject constructor(private val fitbitConnectionHelper: FitbitConnectionHelper) {
    operator fun invoke(): Flow<FitbitState> {
        return fitbitConnectionHelper.state
    }
}