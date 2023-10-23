package com.i2asolutions.athelo.useCase.caregiver

import com.i2asolutions.athelo.network.repository.caregiver.CaregiverRepository
import javax.inject.Inject

class CancelInvitationUseCase @Inject constructor(private val repository: CaregiverRepository) {
    suspend operator fun invoke(invitationId: Int): Boolean {
        return repository.cancelPendingInvitation(invitationId)
    }
}