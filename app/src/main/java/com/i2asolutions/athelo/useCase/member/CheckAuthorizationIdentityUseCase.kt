package com.i2asolutions.athelo.useCase.member

import com.i2asolutions.athelo.network.repository.member.MemberRepository
import com.i2asolutions.athelo.presentation.model.member.AuthorizationIdentity
import javax.inject.Inject

class CheckAuthorizationIdentityUseCase @Inject constructor(private val memberRepository: MemberRepository) {

    suspend operator fun invoke(): List<AuthorizationIdentity> {
        return memberRepository.checkAuthorizationIdentity().map { it.toAuthorizationIdentity() }
    }
}