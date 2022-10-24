package com.i2asolutions.athelo.network.dto.health

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InitDto(@SerialName("url") val url: String)