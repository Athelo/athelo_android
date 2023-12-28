package com.athelohealth.mobile.useCase.health

import com.athelohealth.mobile.network.repository.health.HealthRepository
import com.athelohealth.mobile.presentation.model.exercise.ExerciseEntry
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class LoadExerciseForDataRangeUseCase @Inject constructor(private val repository: HealthRepository) {
    suspend operator fun invoke(
        startDate: Date,
        endDate: Date,
        intervalFunction: String = "DAY",
        patientId: Int?,
    ): List<ExerciseEntry> {
        return repository.getExerciseRecordsForPeriod(
            startDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(startDate),
            endDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(endDate),
            intervalFunction = intervalFunction,
            patientId = patientId,
        ).data.map { it.toExerciseEntry() }
    }
}