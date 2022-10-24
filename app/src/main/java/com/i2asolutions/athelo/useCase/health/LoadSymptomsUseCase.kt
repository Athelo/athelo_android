package com.i2asolutions.athelo.useCase.health

import com.i2asolutions.athelo.network.repository.health.HealthRepository
import com.i2asolutions.athelo.presentation.model.base.PageResponse
import com.i2asolutions.athelo.presentation.model.health.Symptom
import javax.inject.Inject

class LoadSymptomsUseCase @Inject constructor(private val repository: HealthRepository) {
    suspend operator fun invoke(nextUrl: String?): PageResponse<Symptom> {
        return repository.loadSymptoms(nextUrl).toPageResponse { it.toSymptom() }
    }
}