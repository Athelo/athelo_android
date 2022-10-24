package com.i2asolutions.athelo.websocket.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WebSocketErrorDto(@SerialName("message") val message: String? = null)