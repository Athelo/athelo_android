package com.athelohealth.mobile.useCase.appointment

import com.athelohealth.mobile.network.repository.appointment.AppointmentRepository
import com.athelohealth.mobile.presentation.model.appointment.Appointments
import com.athelohealth.mobile.presentation.model.appointment.Providers
import com.athelohealth.mobile.presentation.model.appointment.ProvidersAvailability
import javax.inject.Inject

class LoadProvidersUseCase @Inject constructor(private val repository: AppointmentRepository) {
    suspend operator fun invoke(): Providers {
        return repository.loadProviders().toProviders()
    }

    suspend operator fun invoke(id: String, date: String, timeZone: String): ProvidersAvailability {
        return repository.getProvidersAvailability(id, date, timeZone)
            .toProvidersAvailability()
    }

    suspend operator fun invoke(
        providerId: Int,
        startTime: String,
        endTime: String,
        timeZone: String
    ): List<Appointments.Appointment> {
        return repository.bookAppointment(
            providerId,
            startTime,
            endTime,
            timeZone
        ).toBookAppointment()
    }
}