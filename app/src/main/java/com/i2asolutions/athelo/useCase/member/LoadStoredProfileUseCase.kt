package com.i2asolutions.athelo.useCase.member

import com.i2asolutions.athelo.presentation.model.member.User
import com.i2asolutions.athelo.utils.UserManager
import javax.inject.Inject

class LoadStoredProfileUseCase @Inject constructor(private val userManager: UserManager) {
    suspend operator fun invoke(): User? = userManager.getUser()
}