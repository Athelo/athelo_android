package com.athelohealth.mobile.useCase.application

import com.athelohealth.mobile.network.repository.application.ApplicationRepository
import com.athelohealth.mobile.presentation.model.enums.EnumItem
import javax.inject.Inject

class LoadFeedbackTopicUseCase @Inject constructor(private val repository: ApplicationRepository) {

    suspend operator fun invoke(): List<EnumItem> {
        return repository.loadFeedbackTopic().results.mapNotNull { it.toEnumItem() }
    }
}