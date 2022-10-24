package com.i2asolutions.athelo.useCase.member

import com.i2asolutions.athelo.network.repository.member.MemberRepository
import com.i2asolutions.athelo.presentation.model.member.AuthorizeResult
import javax.inject.Inject

class SignUpUseCase @Inject constructor(private val repository: MemberRepository) {
    suspend operator fun invoke(
        username: String,
        password: String,
        confirmPassword: String
    ): AuthorizeResult =
        repository.register(username = username, password1 = password, password2 = confirmPassword)
            .toAuthorizeResult()
}