package com.athelohealth.mobile.useCase.health

import com.athelohealth.mobile.network.repository.health.HealthRepository
import com.athelohealth.mobile.presentation.model.sleep.SleepEntry
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class LoadSleepEntriesForTodayUseCase @Inject constructor(private val repository: HealthRepository) {
    suspend operator fun invoke(): List<SleepEntry> {
        return repository.getSleepRecordsForDay(
            SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        ).map { it.toSleepEntry() }
    }
}