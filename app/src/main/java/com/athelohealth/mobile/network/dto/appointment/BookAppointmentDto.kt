package com.athelohealth.mobile.network.dto.appointment


import androidx.annotation.Keep
import com.athelohealth.mobile.presentation.model.appointment.Appointments
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Keep
data class BookAppointmentDto(
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
) {

    fun toBookAppointment(): List<Appointments.Appointment> {
        return listOf(
            Appointments.Appointment(
                id = id,
                startTime = startTime,
                endTime = endTime,
                providerName = provider?.displayName,
                providerPhoto = provider?.photo,
                vonageSessionKey = vonageSession,
                zoomHostUrl = zoomHostUrl,
                zoomJoinUrl = zoomJoinUrl
            )
        )
    }
}