package com.i2asolutions.athelo.useCase.health

import com.i2asolutions.athelo.network.repository.health.HealthRepository
import com.i2asolutions.athelo.presentation.model.exercise.ExerciseEntry
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