package com.athelohealth.mobile.useCase.appointment

import com.athelohealth.mobile.network.repository.appointment.AppointmentRepository
import com.athelohealth.mobile.presentation.model.appointment.Appointments
import javax.inject.Inject

class LoadAppointmentsUseCase @Inject constructor(private val repository: AppointmentRepository) {
    suspend operator fun invoke(): Appointments {
         val appointmentsDto = repository.getAppointments()
        return appointmentsDto.toAppointments()
    }
}