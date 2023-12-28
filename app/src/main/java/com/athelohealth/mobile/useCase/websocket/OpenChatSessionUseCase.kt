package com.athelohealth.mobile.useCase.websocket

import com.athelohealth.mobile.network.repository.chat.ChatRepository
import com.athelohealth.mobile.utils.DeviceManager
import javax.inject.Inject

class OpenChatSessionUseCase @Inject constructor(
    private val repository: ChatRepository,
    private val deviceManager: DeviceManager
) {

    suspend operator fun invoke(): String =
        repository.openSession(deviceManager.getIdentifier()).token
}