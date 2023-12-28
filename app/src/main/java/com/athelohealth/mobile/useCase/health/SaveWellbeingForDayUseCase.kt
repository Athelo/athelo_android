package com.athelohealth.mobile.useCase.health

import com.athelohealth.mobile.network.repository.health.HealthRepository
import com.athelohealth.mobile.presentation.model.calendar.Day
import com.athelohealth.mobile.presentation.model.health.Wellbeing
import javax.inject.Inject

class SaveWellbeingForDayUseCase @Inject constructor(val repository: HealthRepository) {
    suspend operator fun invoke(day: Day, feeling: Int): Wellbeing {
        return repository.createWellbeingForDay(
            "${day.year}-${day.month.value}-${day.day}",
            feeling
        ).toWellbeing()
    }
}