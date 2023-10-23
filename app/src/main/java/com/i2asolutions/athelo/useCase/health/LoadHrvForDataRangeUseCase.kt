package com.i2asolutions.athelo.useCase.health

import com.i2asolutions.athelo.network.repository.health.HealthRepository
import com.i2asolutions.athelo.presentation.model.hrv.HrvEntry
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class LoadHrvForDataRangeUseCase @Inject constructor(private val repository: HealthRepository) {
    suspend operator fun invoke(
        startDate: Date,
        endDate: Date,
        patientId: Int?,
        intervalFunction: String = "DAY",
    ): List<HrvEntry> {
        return repository.getHrvRecordsForPeriod(
            startDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(startDate),
            endDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(endDate),
            intervalFunction = intervalFunction,
            patientId = patientId,
        ).map { it.toHrvEntry() }
    }
}