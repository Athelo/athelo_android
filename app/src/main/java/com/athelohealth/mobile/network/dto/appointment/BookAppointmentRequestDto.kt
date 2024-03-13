package com.athelohealth.mobile.network.dto.appointment

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookAppointmentRequestDto(
    @SerialName("provider_id")
    val providerId: Int,
    @SerialName("start_time")
    val startTime: String,
    @SerialName("end_time")
    val endTime: String,
    @SerialName("timezone")
    val timeZone: String
)
