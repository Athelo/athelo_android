package com.i2asolutions.athelo.useCase.health

import com.i2asolutions.athelo.network.repository.health.HealthRepository
import com.i2asolutions.athelo.presentation.model.base.PageResponse
import com.i2asolutions.athelo.presentation.model.calendar.Day
import com.i2asolutions.athelo.presentation.model.health.Wellbeing
import javax.inject.Inject

class LoadWellbeingForDayUseCase @Inject constructor(val repository: HealthRepository) {
    suspend operator fun invoke(nextUrl: String?, day: Day): PageResponse<Wellbeing> {
        return if (nextUrl.isNullOrBlank()) {
            repository.loadWellbeingForDay(
                day = day.day,
                year = day.year,
                month = day.month.value
            ).toPageResponse { it.toWellbeing() }
        } else {
            repository.loadWellbeingForDay(nextUrl)
                .toPageResponse { it.toWellbeing() }
        }
    }
}