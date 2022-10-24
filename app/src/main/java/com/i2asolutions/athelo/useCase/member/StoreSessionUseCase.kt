package com.i2asolutions.athelo.useCase.member

import com.i2asolutions.athelo.presentation.model.member.Token
import com.i2asolutions.athelo.utils.UserManager
import javax.inject.Inject

class StoreSessionUseCase @Inject constructor(private val userManager: UserManager) {
    operator fun invoke(token: Token?) {
        userManager.storeSession(token)
    }
}