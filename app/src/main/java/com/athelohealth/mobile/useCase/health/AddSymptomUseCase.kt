package com.athelohealth.mobile.useCase.health

import com.athelohealth.mobile.network.repository.health.HealthRepository
import com.athelohealth.mobile.presentation.model.calendar.Day
import com.athelohealth.mobile.presentation.model.health.Symptom
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