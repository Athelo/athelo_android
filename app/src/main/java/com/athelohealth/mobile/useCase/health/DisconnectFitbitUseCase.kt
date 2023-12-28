package com.athelohealth.mobile.useCase.health

import com.athelohealth.mobile.network.repository.health.HealthRepository
import javax.inject.Inject

class DisconnectFitbitUseCase @Inject constructor(private val repository: HealthRepository) {

    suspend operator fun invoke(force: Boolean? = null): Boolean {
        val fitbitAccount = repository.loadFitbitAccount().results.firstOrNull()
        return fitbitAccount?.id?.let { id -> repository.disconnectFitbitAccount(id, force) }
            ?: false
    }
}