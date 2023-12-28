package com.athelohealth.mobile.useCase.health

import com.athelohealth.mobile.network.repository.health.HealthRepository
import com.athelohealth.mobile.presentation.model.base.PageResponse
import com.athelohealth.mobile.presentation.model.health.SymptomChronology
import javax.inject.Inject

class LoadSymptomsChronologyUseCase @Inject constructor(private val repository: HealthRepository) {
    suspend operator fun invoke(nextUrl: String?): PageResponse<SymptomChronology> {
        return repository.loadSymptomsChronology(nextUrl)
            .toPageResponse { it.toSymptomChronology() }
    }
}