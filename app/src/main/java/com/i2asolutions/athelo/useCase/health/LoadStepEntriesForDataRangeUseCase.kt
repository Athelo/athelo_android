package com.i2asolutions.athelo.useCase.health

import com.i2asolutions.athelo.network.repository.health.HealthRepository
import com.i2asolutions.athelo.presentation.model.step.StepEntry
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class LoadStepEntriesForDataRangeUseCase @Inject constructor(private val repository: HealthRepository) {
    suspend operator fun invoke(
        startDate: Date,
        endDate: Date,
        intervalFunction: String = "DAY"
    ): List<StepEntry> {
        return repository.getStepRecordsForPeriod(
            SimpleDateFormat("yyyy-MM-dd", Locale.US).format(startDate),
            SimpleDateFormat("yyyy-MM-dd", Locale.US).format(endDate),
            intervalFunction
        ).map { it.toStepEntry() }
    }
}