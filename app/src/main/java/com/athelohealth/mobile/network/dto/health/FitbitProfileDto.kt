package com.athelohealth.mobile.network.dto.health

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FitbitProfileDto(@SerialName("id") val id: Int)
