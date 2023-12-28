package com.athelohealth.mobile.useCase.health

import com.athelohealth.mobile.network.repository.health.HealthRepository
import com.athelohealth.mobile.presentation.model.health.SymptomSummary
import javax.inject.Inject

class LoadSymptomsSummaryUseCase @Inject constructor(private val repository: HealthRepository) {
    suspend operator fun invoke(): List<SymptomSummary> {
        return repository.loadSymptomsSummary().mapNotNull { it.toSymptomSummary() }
    }
}