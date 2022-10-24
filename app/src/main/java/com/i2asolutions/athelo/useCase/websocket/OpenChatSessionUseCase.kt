package com.i2asolutions.athelo.useCase.websocket

import com.i2asolutions.athelo.network.repository.chat.ChatRepository
import com.i2asolutions.athelo.utils.DeviceManager
import javax.inject.Inject

class OpenChatSessionUseCase @Inject constructor(
    private val repository: ChatRepository,
    private val deviceManager: DeviceManager
) {

    suspend operator fun invoke(): String =
        repository.openSession(deviceManager.getIdentifier()).token
}