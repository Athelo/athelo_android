package com.athelohealth.mobile.useCase.websocket

import javax.inject.Inject

class WebSocketSessionUseCases @Inject constructor(
    val openChatSession: OpenChatSessionUseCase,
    val closeChatSession: CloseChatSessionUseCase
)