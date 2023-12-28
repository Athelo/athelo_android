package com.athelohealth.mobile.useCase.websocket

import com.athelohealth.mobile.network.repository.chat.ChatRepository
import javax.inject.Inject

class CloseChatSessionUseCase @Inject constructor(private val repository: ChatRepository) {

    suspend operator fun invoke(sessionToken: String): Boolean =
        repository.closeSession(sessionToken)
}