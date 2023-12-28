package com.athelohealth.mobile.useCase.member.signOut

import com.athelohealth.mobile.utils.fitbit.FitbitConnectionHelper
import javax.inject.Inject

class ClearConnectionStateUseCase @Inject constructor(
    private val fitbitHelper: FitbitConnectionHelper,
) {

    suspend operator fun invoke() {
        fitbitHelper.clearState()
    }
}