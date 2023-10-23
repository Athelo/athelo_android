package com.i2asolutions.athelo.useCase.caregiver

import com.i2asolutions.athelo.network.repository.caregiver.CaregiverRepository
import javax.inject.Inject

class SendInvitationCodeUseCase @Inject constructor(private val repository: CaregiverRepository) {

    suspend operator fun invoke(username: String, email: String, relation: String): Boolean {
        return repository.sendInvitation(username, email, relation)
    }
}