package com.athelohealth.mobile.useCase.health

import com.athelohealth.mobile.network.repository.health.HealthRepository
import com.athelohealth.mobile.presentation.model.health.Symptom
import javax.inject.Inject

class LoadMySymptomUseCase @Inject constructor(private val repository: HealthRepository) {
    suspend operator fun invoke(symptomId: Int): Symptom {
        return repository.loadMySymptom(symptomId).toSymptom()
    }
}