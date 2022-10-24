package com.i2asolutions.athelo.useCase.health

import com.i2asolutions.athelo.network.repository.health.HealthRepository
import com.i2asolutions.athelo.presentation.model.heartRate.HeartRateEntry
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class LoadHeartRateEntriesForDataRangeUseCase @Inject constructor(private val repository: HealthRepository) {
    suspend operator fun invoke(
        startDate: Date,
        endDate: Date,
        intervalFunction: String = "DAY"
    ): List<HeartRateEntry> {
        return repository.getHeartRateRecordsForPeriod(
            SimpleDateFormat("yyyy-MM-dd", Locale.US).format(startDate),
            SimpleDateFormat("yyyy-MM-dd", Locale.US).format(endDate),
            intervalFunction
        ).map { it.toHeartRateEntry() }
    }
}