package com.athelohealth.mobile.websocket.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WebSocketErrorDto(@SerialName("message") val message: String? = null)