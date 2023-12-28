package com.athelohealth.mobile.useCase.patients

import com.athelohealth.mobile.network.repository.caregiver.CaregiverRepository
import com.athelohealth.mobile.presentation.model.base.PageResponse
import com.athelohealth.mobile.presentation.model.patients.Patient
import javax.inject.Inject

class LoadPatientsUseCase @Inject constructor(private val repository: CaregiverRepository) {
    suspend operator fun invoke(nextUrl: String?): PageResponse<Patient> {
        return repository.loadPatients(nextUrl).toPageResponse { it.patient?.toPatient() }
    }
}