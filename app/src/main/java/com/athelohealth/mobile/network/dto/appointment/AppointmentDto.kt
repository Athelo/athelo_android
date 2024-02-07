package com.athelohealth.mobile.network.dto.appointment

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Serializable
@Keep
data class AppointmentDto(
    val end_time: String?,
    val id: Int?,
    val patient: PatientProviderDto?,
    val provider: PatientProviderDto?,
    val start_time: String?,
    val vonage_session: String?,
    val zoom_host_url: String?,
    val zoom_join_url: String?
)
