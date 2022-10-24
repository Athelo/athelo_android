package com.i2asolutions.athelo.useCase.health

import com.i2asolutions.athelo.network.repository.health.HealthRepository
import com.i2asolutions.athelo.presentation.model.calendar.Day
import com.i2asolutions.athelo.presentation.model.health.Wellbeing
import javax.inject.Inject

class SaveWellbeingForDayUseCase @Inject constructor(val repository: HealthRepository) {
    suspend operator fun invoke(day: Day, feeling: Int): Wellbeing {
        return repository.createWellbeingForDay(
            "${day.year}-${day.month.value}-${day.day}",
            feeling
        ).toWellbeing()
    }
}