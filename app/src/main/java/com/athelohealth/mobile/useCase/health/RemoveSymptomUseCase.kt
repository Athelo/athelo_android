package com.athelohealth.mobile.useCase.health

import com.athelohealth.mobile.network.repository.health.HealthRepository
import javax.inject.Inject

class RemoveSymptomUseCase @Inject constructor(private val repository: HealthRepository) {
    suspend operator fun invoke(symptomId: Int): Boolean {
        return repository.removeSymptom(symptomId)
    }
}