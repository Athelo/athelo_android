package com.i2asolutions.athelo.useCase.patients

import com.i2asolutions.athelo.network.repository.caregiver.CaregiverRepository
import javax.inject.Inject

class DeletePatientUseCase @Inject constructor(
    private val repository: CaregiverRepository,
) {

    suspend operator fun invoke(caregiverId: String): Boolean {
        return repository.removePatient(caregiverId)
    }
}