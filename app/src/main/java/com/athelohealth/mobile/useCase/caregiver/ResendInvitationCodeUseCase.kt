package com.athelohealth.mobile.useCase.caregiver

import com.athelohealth.mobile.network.repository.member.MemberRepository
import javax.inject.Inject

class ResendInvitationCodeUseCase @Inject constructor(private val memberRepository: MemberRepository) {
    suspend operator fun invoke(): Boolean {
        return true
    }
}