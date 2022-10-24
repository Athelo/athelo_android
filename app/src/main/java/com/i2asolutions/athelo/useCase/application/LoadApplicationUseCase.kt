package com.i2asolutions.athelo.useCase.application

import com.i2asolutions.athelo.BuildConfig
import com.i2asolutions.athelo.network.repository.application.ApplicationRepository
import com.i2asolutions.athelo.presentation.model.application.Application
import javax.inject.Inject

class LoadApplicationUseCase @Inject constructor(private val repository: ApplicationRepository) {

    suspend operator fun invoke(): Application? {
        return repository.loadApplications().results
            .firstOrNull { it.identifier == BuildConfig.APP_ID }
            ?.toApplication()
    }
}