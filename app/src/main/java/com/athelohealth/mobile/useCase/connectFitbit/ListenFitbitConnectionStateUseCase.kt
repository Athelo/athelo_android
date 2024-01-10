package com.athelohealth.mobile.useCase.connectFitbit

import com.athelohealth.mobile.utils.fitbit.FitbitConnectionHelper
import com.athelohealth.mobile.utils.fitbit.FitbitState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ListenFitbitConnectionStateUseCase @Inject constructor(private val fitbitConnectionHelper: FitbitConnectionHelper) {
    operator fun invoke(): Flow<FitbitState> {
        return fitbitConnectionHelper.viewState
    }
}