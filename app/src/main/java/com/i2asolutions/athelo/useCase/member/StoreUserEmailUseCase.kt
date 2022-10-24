package com.i2asolutions.athelo.useCase.member

import com.i2asolutions.athelo.utils.UserManager
import javax.inject.Inject

class StoreUserEmailUseCase @Inject constructor(private val userManager: UserManager) {
    operator fun invoke(email: String?) {
        userManager.storeUserEmail(email)
    }
}