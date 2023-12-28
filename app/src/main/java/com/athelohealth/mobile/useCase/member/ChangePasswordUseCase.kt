package com.athelohealth.mobile.useCase.member

import com.athelohealth.mobile.network.repository.member.MemberRepository
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(private val repository: MemberRepository) {
    suspend operator fun invoke(
        newPassword: String,
        repeatNewPassword: String,
        oldPassword: String
    ): String {
        return repository.changePassword(
            oldPassword = oldPassword,
            newPassword = newPassword,
            repeatPassword = repeatNewPassword
        )
    }
}