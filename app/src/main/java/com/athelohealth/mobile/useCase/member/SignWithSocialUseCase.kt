package com.athelohealth.mobile.useCase.member

import com.athelohealth.mobile.network.repository.member.MemberRepository
import com.athelohealth.mobile.presentation.model.member.AuthorizeResult
import javax.inject.Inject

class SignWithSocialUseCase @Inject constructor(private val repository: MemberRepository) {
    suspend operator fun invoke(token: String, type: String): AuthorizeResult {
        return repository.loginSocial(token, type).toAuthorizeResult()
    }
}