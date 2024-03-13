package com.athelohealth.mobile.network.dto.appointment


import androidx.annotation.Keep
import com.athelohealth.mobile.presentation.model.appointment.JoinAppointmentToken
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Keep
data class JoinAppointmentTokenDto(
    @SerialName("token")
    val token: String?
) {

    fun toJoinAppointmentToken() = JoinAppointmentToken(token)
}