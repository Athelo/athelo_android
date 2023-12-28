package com.athelohealth.mobile.useCase.member

import com.athelohealth.mobile.utils.UserManager
import javax.inject.Inject

class StoreUserEmailUseCase @Inject constructor(private val userManager: UserManager) {
    operator fun invoke(email: String?) {
        userManager.storeUserEmail(email)
    }
}