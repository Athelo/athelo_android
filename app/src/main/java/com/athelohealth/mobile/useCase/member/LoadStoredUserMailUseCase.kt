package com.athelohealth.mobile.useCase.member

import com.athelohealth.mobile.utils.UserManager
import javax.inject.Inject

class LoadStoredUserMailUseCase @Inject constructor(private val userManager: UserManager) {
    suspend operator fun invoke(): String? = userManager.loadUserEmail()
}