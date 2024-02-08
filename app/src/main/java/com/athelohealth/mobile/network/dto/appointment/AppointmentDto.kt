package com.athelohealth.mobile.network.dto.appointment

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Keep
data class AppointmentDto(
    @SerialName("end_time")
    val endTime: String?,
    @SerialName("id")
    val id: Int?,
    @SerialName("patient")
    val patient: PatientProviderDto?,
    @SerialName("provider")
    val provider: PatientProviderDto?,
    @SerialName("start_time")
    val startTime: String?,
    @SerialName("vonage_session")
    val vonageSession: String?,
    @SerialName("zoom_host_url")
    val zoomHostUrl: String?,
    @SerialName("zoom_join_url")
    val zoomJoinUrl: String?
)
