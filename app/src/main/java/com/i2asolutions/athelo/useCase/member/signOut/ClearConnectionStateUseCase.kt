package com.i2asolutions.athelo.useCase.member.signOut

import com.i2asolutions.athelo.utils.fitbit.FitbitConnectionHelper
import javax.inject.Inject

class ClearConnectionStateUseCase @Inject constructor(
    private val fitbitHelper: FitbitConnectionHelper,
) {

    suspend operator fun invoke() {
        fitbitHelper.clearState()
    }
}