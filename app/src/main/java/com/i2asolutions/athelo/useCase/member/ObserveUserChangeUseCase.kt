package com.i2asolutions.athelo.useCase.member

import com.i2asolutions.athelo.presentation.model.member.User
import com.i2asolutions.athelo.utils.UserManager
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ObserveUserChangeUseCase @Inject constructor(private val userManager: UserManager) {
    operator fun invoke(): StateFlow<User?> {
        return userManager.user
    }
}