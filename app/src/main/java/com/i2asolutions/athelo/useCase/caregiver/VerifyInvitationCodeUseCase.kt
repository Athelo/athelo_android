package com.i2asolutions.athelo.useCase.caregiver

import com.i2asolutions.athelo.network.repository.caregiver.CaregiverRepository
import com.i2asolutions.athelo.network.repository.member.MemberRepository
import javax.inject.Inject

class VerifyInvitationCodeUseCase @Inject constructor(private val repository: CaregiverRepository) {
    suspend operator fun invoke(code: String): Boolean {
        return repository.confirmInvitation(code)
    }
}