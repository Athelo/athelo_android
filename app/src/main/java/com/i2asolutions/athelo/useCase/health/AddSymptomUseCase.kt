package com.i2asolutions.athelo.useCase.health

import com.i2asolutions.athelo.network.repository.health.HealthRepository
import com.i2asolutions.athelo.presentation.model.calendar.Day
import com.i2asolutions.athelo.presentation.model.health.Symptom
import javax.inject.Inject

class AddSymptomUseCase @Inject constructor(private val repository: HealthRepository) {
    suspend operator fun invoke(symptom: Symptom, note: String, date: Day): Symptom {
        return repository.addSymptom(
            symptom.id,
            note,
            "${date.year}-${date.month.value}-${date.day}"
        ).toSymptom()
    }
}