package com.athelohealth.mobile.useCase.member

import com.athelohealth.mobile.network.repository.member.MemberRepository
import com.athelohealth.mobile.presentation.model.member.AuthorizationIdentity
import com.athelohealth.mobile.presentation.model.member.IdentityType
import javax.inject.Inject

class CheckAuthorizationIdentityUseCase @Inject constructor(private val memberRepository: MemberRepository) {

    suspend operator fun invoke(): List<AuthorizationIdentity> {
        return listOf(AuthorizationIdentity(IdentityType.Native))
    }
}