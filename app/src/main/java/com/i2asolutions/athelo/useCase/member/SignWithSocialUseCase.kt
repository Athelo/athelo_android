package com.i2asolutions.athelo.useCase.member

import com.i2asolutions.athelo.network.repository.member.MemberRepository
import com.i2asolutions.athelo.presentation.model.member.AuthorizeResult
import javax.inject.Inject

class SignWithSocialUseCase @Inject constructor(private val repository: MemberRepository) {
    suspend operator fun invoke(token: String, type: String): AuthorizeResult {
        return repository.loginSocial(token, type).toAuthorizeResult()
    }
}