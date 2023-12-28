package com.athelohealth.mobile.useCase.member

import com.athelohealth.mobile.presentation.model.member.User
import com.athelohealth.mobile.utils.UserManager
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ObserveUserChangeUseCase @Inject constructor(private val userManager: UserManager) {
    operator fun invoke(): StateFlow<User?> {
        return userManager.user
    }
}