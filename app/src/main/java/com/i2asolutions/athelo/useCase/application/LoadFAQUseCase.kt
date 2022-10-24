package com.i2asolutions.athelo.useCase.application

import com.i2asolutions.athelo.network.repository.application.ApplicationRepository
import com.i2asolutions.athelo.presentation.model.application.FAQ
import com.i2asolutions.athelo.presentation.model.base.PageResponse
import javax.inject.Inject

class LoadFAQUseCase @Inject constructor(private val repository: ApplicationRepository) {
    suspend operator fun invoke(next: String?): PageResponse<FAQ> {
        return repository.loadFAQ(next).toPageResponse { it.toFAQ() }
    }
}