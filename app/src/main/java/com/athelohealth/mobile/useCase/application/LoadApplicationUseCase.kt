package com.athelohealth.mobile.useCase.application

import com.athelohealth.mobile.BuildConfig
import com.athelohealth.mobile.network.repository.application.ApplicationRepository
import com.athelohealth.mobile.presentation.model.application.Application
import javax.inject.Inject

class LoadApplicationUseCase @Inject constructor(private val repository: ApplicationRepository) {

    suspend operator fun invoke(): Application? {
        return repository.loadApplications().results
            .firstOrNull()
            ?.toApplication()
    }
}