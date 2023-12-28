package com.athelohealth.mobile.useCase.member

import com.athelohealth.mobile.presentation.model.member.AuthorizationState
import com.athelohealth.mobile.utils.UserManager
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class LoadUserStateUseCase @Inject constructor(private val userManager: UserManager) {
    operator fun invoke(): StateFlow<AuthorizationState> {
        return userManager.logInState
    }
}