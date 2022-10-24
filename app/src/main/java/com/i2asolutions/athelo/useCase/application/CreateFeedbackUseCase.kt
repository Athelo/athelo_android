package com.i2asolutions.athelo.useCase.application

import com.i2asolutions.athelo.network.repository.application.ApplicationRepository
import javax.inject.Inject

class CreateFeedbackUseCase @Inject constructor(private val repository: ApplicationRepository) {
    suspend operator fun invoke(topicId: String, content: String): Boolean {
        return repository.createFeedback(topicId, content)
    }
}