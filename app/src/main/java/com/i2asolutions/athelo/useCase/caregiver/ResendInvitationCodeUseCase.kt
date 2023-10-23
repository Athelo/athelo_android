package com.i2asolutions.athelo.useCase.caregiver

import com.i2asolutions.athelo.network.repository.member.MemberRepository
import javax.inject.Inject

class ResendInvitationCodeUseCase @Inject constructor(private val memberRepository: MemberRepository) {
    suspend operator fun invoke(): Boolean {
        return true
    }
}