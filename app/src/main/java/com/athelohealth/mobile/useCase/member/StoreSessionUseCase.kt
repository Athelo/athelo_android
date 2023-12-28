package com.athelohealth.mobile.useCase.member

import com.athelohealth.mobile.presentation.model.member.Token
import com.athelohealth.mobile.utils.UserManager
import javax.inject.Inject

class StoreSessionUseCase @Inject constructor(private val userManager: UserManager) {
    operator fun invoke(token: Token?) {
        userManager.storeSession(token)
    }
}