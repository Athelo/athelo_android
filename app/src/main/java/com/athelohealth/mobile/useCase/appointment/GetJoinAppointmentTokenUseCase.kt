package com.athelohealth.mobile.useCase.appointment

import com.athelohealth.mobile.network.repository.appointment.AppointmentRepository
import com.athelohealth.mobile.presentation.model.appointment.JoinAppointmentToken
import javax.inject.Inject

class GetJoinAppointmentTokenUseCase @Inject constructor(
    private val repository: AppointmentRepository
) {

    suspend operator fun invoke(appointmentId:Int): JoinAppointmentToken {
        return repository.getJoinAppointmentToken(appointmentId).toJoinAppointmentToken()
    }
}