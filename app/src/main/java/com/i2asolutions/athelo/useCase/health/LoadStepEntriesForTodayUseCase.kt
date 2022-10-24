package com.i2asolutions.athelo.useCase.health

import com.i2asolutions.athelo.network.repository.health.HealthRepository
import com.i2asolutions.athelo.presentation.model.step.StepEntry
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class LoadStepEntriesForTodayUseCase @Inject constructor(private val repository: HealthRepository) {
    suspend operator fun invoke(): List<StepEntry> {
        val calendar = Calendar.getInstance()
        return repository.getStepRecordsForDay(
            SimpleDateFormat("yyyy-MM-dd", Locale.US).format(calendar.time)
        ).map { it.toStepEntry() }
    }
}