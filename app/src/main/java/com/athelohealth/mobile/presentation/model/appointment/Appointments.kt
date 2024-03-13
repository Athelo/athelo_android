package com.athelohealth.mobile.presentation.model.appointment

import androidx.annotation.Keep

@Keep
data class Appointments(
    val next: String?,
    val appointments: List<Appointment>?
) {

    @Keep
    data class Appointment(
        val id: Int?,
        val startTime: String?,
        val endTime: String?,
        val providerName: String?,
        val providerPhoto: String?,
        val vonageSessionKey: String?,
        val zoomHostUrl: String?,
        val zoomJoinUrl: String?
    )
}
