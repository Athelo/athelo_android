package com.i2asolutions.athelo.useCase.websocket

import com.i2asolutions.athelo.network.repository.chat.ChatRepository
import javax.inject.Inject

class CloseChatSessionUseCase @Inject constructor(private val repository: ChatRepository) {

    suspend operator fun invoke(sessionToken: String): Boolean =
        repository.closeSession(sessionToken)
}