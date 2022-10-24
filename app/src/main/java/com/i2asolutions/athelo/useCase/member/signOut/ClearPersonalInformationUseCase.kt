package com.i2asolutions.athelo.useCase.member.signOut

import com.i2asolutions.athelo.utils.PreferenceHelper
import javax.inject.Inject

class ClearPersonalInformationUseCase @Inject constructor(
    private val preferenceHelper: PreferenceHelper,
) {

    suspend operator fun invoke() {
        preferenceHelper.showChatGroupLanding = true
    }
}