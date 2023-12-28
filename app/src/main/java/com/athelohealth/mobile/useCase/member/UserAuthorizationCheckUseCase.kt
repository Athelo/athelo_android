package com.athelohealth.mobile.useCase.member

import com.athelohealth.mobile.utils.UserManager
import javax.inject.Inject

class UserAuthorizationCheckUseCase @Inject constructor(private val userManager: UserManager) {
    operator fun invoke() {
        userManager.checkLogInState()
    }
}