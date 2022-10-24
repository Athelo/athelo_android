package com.i2asolutions.athelo.useCase.health

import com.i2asolutions.athelo.network.repository.health.HealthRepository
import javax.inject.Inject

class DisconnectFitbitUseCase @Inject constructor(private val repository: HealthRepository) {

    suspend operator fun invoke(): Boolean {
        val fitbitAccount = repository.loadFitbitAccount().results.firstOrNull()
        return fitbitAccount?.id?.let { id -> repository.disconnectFitbitAccount(id) } ?: false
    }
}