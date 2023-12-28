package com.athelohealth.mobile.useCase.application

import com.athelohealth.mobile.network.repository.application.ApplicationRepository
import com.athelohealth.mobile.presentation.model.application.FAQ
import com.athelohealth.mobile.presentation.model.base.PageResponse
import javax.inject.Inject

class LoadFAQUseCase @Inject constructor(private val repository: ApplicationRepository) {
    suspend operator fun invoke(next: String?): PageResponse<FAQ> {
        return repository.loadFAQ(next).toPageResponse { it.toFAQ() }
    }
}