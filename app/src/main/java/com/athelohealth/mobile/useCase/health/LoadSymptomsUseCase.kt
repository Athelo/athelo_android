package com.athelohealth.mobile.useCase.health

import com.athelohealth.mobile.network.repository.health.HealthRepository
import com.athelohealth.mobile.presentation.model.base.PageResponse
import com.athelohealth.mobile.presentation.model.health.Symptom
import javax.inject.Inject

class LoadSymptomsUseCase @Inject constructor(private val repository: HealthRepository) {
    suspend operator fun invoke(nextUrl: String?): PageResponse<Symptom> {
        return repository.loadSymptoms(nextUrl).toPageResponse { it.toSymptom() }
    }
}