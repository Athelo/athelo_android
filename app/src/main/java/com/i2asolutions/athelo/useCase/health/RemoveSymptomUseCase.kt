package com.i2asolutions.athelo.useCase.health

import com.i2asolutions.athelo.network.repository.health.HealthRepository
import javax.inject.Inject

class RemoveSymptomUseCase @Inject constructor(private val repository: HealthRepository) {
    suspend operator fun invoke(symptomId: Int): Boolean {
        return repository.removeSymptom(symptomId)
    }
}