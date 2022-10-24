package com.i2asolutions.athelo.useCase.health

import com.i2asolutions.athelo.network.repository.health.HealthRepository
import com.i2asolutions.athelo.presentation.model.health.Symptom
import javax.inject.Inject

class LoadMySymptomUseCase @Inject constructor(private val repository: HealthRepository) {
    suspend operator fun invoke(symptomId: Int): Symptom {
        return repository.loadMySymptom(symptomId).toSymptom()
    }
}