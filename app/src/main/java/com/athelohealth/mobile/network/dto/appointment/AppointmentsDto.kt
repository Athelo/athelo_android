package com.athelohealth.mobile.network.dto.appointment


import androidx.annotation.Keep
import com.athelohealth.mobile.presentation.model.appointment.Appointments
import kotlinx.serialization.Serializable

@Serializable
@Keep
data class AppointmentsDto(
    val count: Int?,
    val next: String?,
    val previous: String?,
    val results: List<AppointmentDto>?
) {

    fun toAppointments(): Appointments {
        return Appointments(
            next,
            appointments = results?.map {
                it.toAppointment()
            }
        )
    }

    private fun AppointmentDto.toAppointment() =
        Appointments.Appointment(
            id = id,
            startTime = start_time,
            endTime = end_time,
            providerName = provider?.display_name,
            providerPhoto = provider?.photo,
            vonageSessionKey = vonage_session,
            zoomHostUrl = zoom_host_url,
            zoomJoinUrl = zoom_join_url
        )
}