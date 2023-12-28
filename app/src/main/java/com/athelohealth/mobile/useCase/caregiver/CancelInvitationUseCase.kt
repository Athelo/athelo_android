package com.athelohealth.mobile.useCase.caregiver

import com.athelohealth.mobile.network.repository.caregiver.CaregiverRepository
import javax.inject.Inject

class CancelInvitationUseCase @Inject constructor(private val repository: CaregiverRepository) {
    suspend operator fun invoke(invitationId: Int): Boolean {
        return repository.cancelPendingInvitation(invitationId)
    }
}