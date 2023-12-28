package com.athelohealth.mobile.useCase.application

import com.athelohealth.mobile.network.repository.application.ApplicationRepository
import javax.inject.Inject

class CreateFeedbackUseCase @Inject constructor(private val repository: ApplicationRepository) {
    suspend operator fun invoke(topicId: String, content: String): Boolean {
        return repository.createFeedback(topicId, content)
    }
}