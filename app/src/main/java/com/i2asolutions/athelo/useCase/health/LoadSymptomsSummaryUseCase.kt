package com.i2asolutions.athelo.useCase.health

import com.i2asolutions.athelo.network.repository.health.HealthRepository
import com.i2asolutions.athelo.presentation.model.health.SymptomSummary
import javax.inject.Inject

class LoadSymptomsSummaryUseCase @Inject constructor(private val repository: HealthRepository) {
    suspend operator fun invoke(): List<SymptomSummary> {
        return repository.loadSymptomsSummary().mapNotNull { it.toSymptomSummary() }
    }
}