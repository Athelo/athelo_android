package com.i2asolutions.athelo.network.dto.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ChatTokenDto(@SerialName("token") val token: String)
