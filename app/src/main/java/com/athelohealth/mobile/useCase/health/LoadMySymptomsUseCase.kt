package com.athelohealth.mobile.useCase.health

import com.athelohealth.mobile.network.repository.health.HealthRepository
import com.athelohealth.mobile.presentation.model.base.PageResponse
import com.athelohealth.mobile.presentation.model.calendar.Day
import com.athelohealth.mobile.presentation.model.health.Symptom
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