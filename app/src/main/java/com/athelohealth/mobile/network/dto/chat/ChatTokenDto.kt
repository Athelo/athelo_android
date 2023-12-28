package com.athelohealth.mobile.network.dto.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ChatTokenDto(@SerialName("token") val token: String)
