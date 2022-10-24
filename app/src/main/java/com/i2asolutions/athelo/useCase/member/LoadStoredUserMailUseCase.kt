package com.i2asolutions.athelo.useCase.member

import com.i2asolutions.athelo.utils.UserManager
import javax.inject.Inject

class LoadStoredUserMailUseCase @Inject constructor(private val userManager: UserManager) {
    suspend operator fun invoke(): String? = userManager.loadUserEmail()
}