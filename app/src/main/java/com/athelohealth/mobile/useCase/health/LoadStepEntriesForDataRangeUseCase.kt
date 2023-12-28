package com.athelohealth.mobile.useCase.health

import com.athelohealth.mobile.network.repository.health.HealthRepository
import com.athelohealth.mobile.presentation.model.step.StepEntry
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class LoadStepEntriesForDataRangeUseCase @Inject constructor(private val repository: HealthRepository) {
    suspend operator fun invoke(
        startDate: Date,
        endDate: Date,
        intervalFunction: String = "DAY",
        patientId: Int?,
    ): List<StepEntry> {
        return repository.getStepRecordsForPeriod(
            startDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(startDate),
            endDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(endDate),
            intervalFunction = intervalFunction,
            patientId = patientId
        ).map { it.toStepEntry() }
    }
}