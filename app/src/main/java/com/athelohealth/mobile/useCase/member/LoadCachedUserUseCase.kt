package com.athelohealth.mobile.useCase.member

import com.athelohealth.mobile.presentation.model.member.User
import com.athelohealth.mobile.utils.UserManager
import javax.inject.Inject

class LoadCachedUserUseCase @Inject constructor(private val userManager: UserManager) {
    suspend operator fun invoke(): User? {
        return userManager.getUser()
    }
}