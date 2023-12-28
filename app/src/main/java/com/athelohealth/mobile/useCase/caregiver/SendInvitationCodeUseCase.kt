package com.athelohealth.mobile.useCase.caregiver

import com.athelohealth.mobile.network.repository.caregiver.CaregiverRepository
import javax.inject.Inject

class SendInvitationCodeUseCase @Inject constructor(private val repository: CaregiverRepository) {

    suspend operator fun invoke(username: String, email: String, relation: String): Boolean {
        return repository.sendInvitation(username, email, relation)
    }
}