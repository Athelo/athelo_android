package com.i2asolutions.athelo.useCase.caregiver

import com.i2asolutions.athelo.network.repository.caregiver.CaregiverRepository
import com.i2asolutions.athelo.network.repository.chat.ChatRepository
import com.i2asolutions.athelo.presentation.model.base.PageResponse
import com.i2asolutions.athelo.presentation.model.patients.Patient
import javax.inject.Inject

class LoadPatientsUseCase @Inject constructor(
    private val repository: CaregiverRepository,
    private val chatRepository: ChatRepository
) {

    suspend operator fun invoke(nextUrl: String?): PageResponse<Patient> {
        return repository.loadPatients(nextUrl).toPageResponse { it.patient?.toPatient() }
    }
}