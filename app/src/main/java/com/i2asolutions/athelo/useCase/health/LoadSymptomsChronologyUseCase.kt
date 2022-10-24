package com.i2asolutions.athelo.useCase.health

import com.i2asolutions.athelo.network.repository.health.HealthRepository
import com.i2asolutions.athelo.presentation.model.base.PageResponse
import com.i2asolutions.athelo.presentation.model.health.SymptomChronology
import javax.inject.Inject

class LoadSymptomsChronologyUseCase @Inject constructor(private val repository: HealthRepository) {
    suspend operator fun invoke(nextUrl: String?): PageResponse<SymptomChronology> {
        return repository.loadSymptomsChronology(nextUrl)
            .toPageResponse { it.toSymptomChronology() }
    }
}