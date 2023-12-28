package com.athelohealth.mobile.useCase.caregiver

import com.athelohealth.mobile.network.repository.caregiver.CaregiverRepository
import com.athelohealth.mobile.network.repository.member.MemberRepository
import javax.inject.Inject

class VerifyInvitationCodeUseCase @Inject constructor(private val repository: CaregiverRepository) {
    suspend operator fun invoke(code: String): Boolean {
        return repository.confirmInvitation(code)
    }
}