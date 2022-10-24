package com.i2asolutions.athelo.useCase.health

import com.i2asolutions.athelo.network.repository.health.HealthRepository
import com.i2asolutions.athelo.presentation.model.base.PageResponse
import com.i2asolutions.athelo.presentation.model.calendar.Day
import com.i2asolutions.athelo.presentation.model.health.Symptom
import javax.inject.Inject

class LoadMySymptomsUseCase @Inject constructor(private val repository: HealthRepository) {
    suspend operator fun invoke(nextUrl: String?, day: Day): PageResponse<Symptom> {
        return (nextUrl?.let { repository.loadMySymptoms(it) } ?: repository.loadMySymptoms(
            day = day.day,
            year = day.year,
            month = day.month.value
        )).toPageResponse { it.toSymptom() }
    }
}