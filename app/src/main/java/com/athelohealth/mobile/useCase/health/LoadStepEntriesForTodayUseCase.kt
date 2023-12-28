package com.athelohealth.mobile.useCase.health

import com.athelohealth.mobile.network.repository.health.HealthRepository
import com.athelohealth.mobile.presentation.model.step.StepEntry
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