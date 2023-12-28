package com.athelohealth.mobile.useCase.caregiver

import com.athelohealth.mobile.network.repository.caregiver.CaregiverRepository
import javax.inject.Inject

class DeleteCaregiverUseCase @Inject constructor(
    private val repository: CaregiverRepository,
) {

    suspend operator fun invoke(caregiverId: Int): Boolean {
        return repository.removeCaregiver(caregiverId)
    }
}