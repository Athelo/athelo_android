package com.athelohealth.mobile.useCase.member.signOut

import com.athelohealth.mobile.utils.UserManager
import javax.inject.Inject

class ClearUserInformationUseCase @Inject constructor(private val userManager: UserManager) {

    suspend operator fun invoke() {
        userManager.logout()
    }
}