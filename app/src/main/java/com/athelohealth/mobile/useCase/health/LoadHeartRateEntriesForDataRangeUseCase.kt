package com.athelohealth.mobile.useCase.health

import com.athelohealth.mobile.network.repository.health.HealthRepository
import com.athelohealth.mobile.presentation.model.heartRate.HeartRateEntry
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class LoadHeartRateEntriesForDataRangeUseCase @Inject constructor(private val repository: HealthRepository) {
    suspend operator fun invoke(
        startDate: Date,
        endDate: Date,
        intervalFunction: String = "DAY",
        patientId: Int?,
    ): List<HeartRateEntry> {
        return repository.getHeartRateRecordsForPeriod(
            startDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(startDate),
            endDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(endDate),
            intervalFunction = intervalFunction,
            patientId = patientId,
        ).map { it.toHeartRateEntry() }
    }
}