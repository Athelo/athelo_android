package com.athelohealth.mobile.useCase.health

import com.athelohealth.mobile.network.repository.health.HealthRepository
import com.athelohealth.mobile.presentation.model.sleep.SleepEntry
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class LoadSleepEntriesForDataRangeUseCase @Inject constructor(private val repository: HealthRepository) {
    suspend operator fun invoke(startDate: Date, endDate: Date, patientId: Int?): List<SleepEntry> {
        return repository.getSleepRecordsForPeriod(
            SimpleDateFormat("yyyy-MM-dd", Locale.US).format(startDate),
            SimpleDateFormat("yyyy-MM-dd", Locale.US).format(endDate),
            patientId = patientId,
        ).map { it.toSleepEntry() }
    }
}