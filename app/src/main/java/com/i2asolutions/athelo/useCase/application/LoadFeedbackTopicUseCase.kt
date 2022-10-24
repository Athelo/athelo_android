package com.i2asolutions.athelo.useCase.application

import com.i2asolutions.athelo.network.repository.application.ApplicationRepository
import com.i2asolutions.athelo.presentation.model.enums.EnumItem
import javax.inject.Inject

class LoadFeedbackTopicUseCase @Inject constructor(private val repository: ApplicationRepository) {

    suspend operator fun invoke(): List<EnumItem> {
        return repository.loadFeedbackTopic().results.mapNotNull { it.toEnumItem() }
    }
}