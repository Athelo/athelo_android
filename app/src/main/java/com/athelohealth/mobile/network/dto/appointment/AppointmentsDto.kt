package com.athelohealth.mobile.network.dto.appointment


import androidx.annotation.Keep
import com.athelohealth.mobile.presentation.model.appointment.Appointments
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Keep
data class AppointmentsDto(
    @SerialName("count")
    val count: Int?,
    @SerialName("next")
    val next: String?,
    @SerialName("previous")
    val previous: String?,
    @SerialName("results")
    val appointments: List<AppointmentDto>?
) {

    fun toAppointments(): Appointments {
        return Appointments(
            next,
            appointments = appointments?.map {
                it.toAppointment()
            }
        )
    }

    private fun AppointmentDto.toAppointment() =
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
}