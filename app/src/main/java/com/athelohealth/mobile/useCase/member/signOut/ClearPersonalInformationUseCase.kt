package com.athelohealth.mobile.useCase.member.signOut

import com.athelohealth.mobile.utils.PreferenceHelper
import javax.inject.Inject

class ClearPersonalInformationUseCase @Inject constructor(
    private val preferenceHelper: PreferenceHelper,
) {

    suspend operator fun invoke() {
        preferenceHelper.showChatGroupLanding = true
    }
}