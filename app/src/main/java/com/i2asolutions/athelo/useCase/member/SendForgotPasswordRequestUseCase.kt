package com.i2asolutions.athelo.useCase.member

import com.i2asolutions.athelo.network.repository.member.MemberRepository
import com.i2asolutions.athelo.presentation.model.member.ResetPasswordResult
import javax.inject.Inject

class SendForgotPasswordRequestUseCase @Inject constructor(private val repository: MemberRepository) {
    suspend operator fun invoke(email: String): ResetPasswordResult =
        repository.resetPasswordRequest(email).toResetPasswordResult()
}