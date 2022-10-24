package com.i2asolutions.athelo.useCase.health

import com.i2asolutions.athelo.network.repository.health.HealthRepository
import com.i2asolutions.athelo.presentation.model.sleep.SleepEntry
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class LoadSleepEntriesForDataRangeUseCase @Inject constructor(private val repository: HealthRepository) {
    suspend operator fun invoke(startDate: Date, endDate: Date): List<SleepEntry> {
        return repository.getSleepRecordsForPeriod(
            SimpleDateFormat("yyyy-MM-dd", Locale.US).format(startDate),
            SimpleDateFormat("yyyy-MM-dd", Locale.US).format(endDate),
        ).map { it.toSleepEntry() }
    }
}