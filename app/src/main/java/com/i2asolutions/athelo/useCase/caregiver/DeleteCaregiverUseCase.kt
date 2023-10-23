package com.i2asolutions.athelo.useCase.caregiver

import com.i2asolutions.athelo.network.repository.caregiver.CaregiverRepository
import javax.inject.Inject

class DeleteCaregiverUseCase @Inject constructor(
    private val repository: CaregiverRepository,
) {

    suspend operator fun invoke(caregiverId: Int): Boolean {
        return repository.removeCaregiver(caregiverId)
    }
}