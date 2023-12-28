package com.athelohealth.mobile.useCase.patients

import com.athelohealth.mobile.network.repository.caregiver.CaregiverRepository
import javax.inject.Inject

class DeletePatientUseCase @Inject constructor(
    private val repository: CaregiverRepository,
) {

    suspend operator fun invoke(caregiverId: String): Boolean {
        return repository.removePatient(caregiverId)
    }
}