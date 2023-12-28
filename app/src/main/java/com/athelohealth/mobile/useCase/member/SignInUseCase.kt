package com.athelohealth.mobile.useCase.member

import com.athelohealth.mobile.network.repository.member.MemberRepository
import com.athelohealth.mobile.presentation.model.member.AuthorizeResult
import javax.inject.Inject

class SignInUseCase @Inject constructor(private val repository: MemberRepository) {
    suspend operator fun invoke(username: String, password: String): AuthorizeResult {
        return repository.login(username, password).toAuthorizeResult()
    }
}