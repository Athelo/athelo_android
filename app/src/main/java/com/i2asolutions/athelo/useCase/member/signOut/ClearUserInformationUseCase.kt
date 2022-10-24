package com.i2asolutions.athelo.useCase.member.signOut

import com.i2asolutions.athelo.utils.UserManager
import javax.inject.Inject

class ClearUserInformationUseCase @Inject constructor(private val userManager: UserManager) {

    suspend operator fun invoke() {
        userManager.logout()
    }
}