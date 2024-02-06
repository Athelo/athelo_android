package com.athelohealth.mobile.network.dto.appointment


import androidx.annotation.Keep
import com.athelohealth.mobile.presentation.model.appointment.Appointments
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
@Keep
data class AppointmentsDto(
    @SerializedName("count")
    val count: Int?,
    @SerializedName("next")
    val next: String?,
    @SerializedName("previous")
    val previous: String?,
    @SerializedName("results")
    val appointments: List<AppointmentDto>?
) {

    @Serializable
    @Keep
    data class AppointmentDto(
        @SerializedName("end_time")
        val endTime: String?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("patient")
        val patient: PatientProviderDto?,
        @SerializedName("provider")
        val provider: PatientProviderDto?,
        @SerializedName("start_time")
        val startTime: String?,
        @SerializedName("vonage_session")
        val vonageSession: String?,
        @SerializedName("zoom_host_url")
        val zoomHostUrl: String?,
        @SerializedName("zoom_join_url")
        val zoomJoinUrl: String?
    ) {

        @Serializable
        @Keep
        data class PatientProviderDto(
            @SerializedName("display_name") val displayName: String,
            @SerializedName("photo") val photo: String
        )
    }

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