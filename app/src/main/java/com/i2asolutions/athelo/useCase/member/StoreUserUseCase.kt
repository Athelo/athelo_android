package com.i2asolutions.athelo.useCase.member

import com.i2asolutions.athelo.presentation.model.member.User
import com.i2asolutions.athelo.utils.UserManager
import javax.inject.Inject

class StoreUserUseCase @Inject constructor(private val userManager: UserManager) {
    suspend operator fun invoke(user: User?) {
        userManager.storeUser(user)
    }
}