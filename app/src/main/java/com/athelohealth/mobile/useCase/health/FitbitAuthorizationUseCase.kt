package com.athelohealth.mobile.useCase.health

import com.athelohealth.mobile.network.repository.health.HealthRepository
import javax.inject.Inject

class FitbitAuthorizationUseCase @Inject constructor(private val repository: HealthRepository) {
    suspend operator fun invoke(): String {
        return repository.connectFitbitAccount().url
    }
}