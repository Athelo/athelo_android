package com.athelohealth.mobile.useCase.member

import com.athelohealth.mobile.network.repository.member.MemberRepository
import com.athelohealth.mobile.presentation.model.member.ResetPasswordResult
import javax.inject.Inject

class SendForgotPasswordRequestUseCase @Inject constructor(private val repository: MemberRepository) {
    suspend operator fun invoke(email: String): ResetPasswordResult =
        repository.resetPasswordRequest(email).toResetPasswordResult()
}