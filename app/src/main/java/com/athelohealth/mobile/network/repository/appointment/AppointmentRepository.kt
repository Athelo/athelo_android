package com.athelohealth.mobile.network.repository.appointment

import com.athelohealth.mobile.network.dto.appointment.AppointmentsDto
import com.athelohealth.mobile.network.dto.appointment.ProvidersAvailabilityDto
import com.athelohealth.mobile.network.dto.appointment.ProvidersDto

interface AppointmentRepository {

    suspend fun loadProviders(): ProvidersDto
    suspend fun getProvidersAvailability(date: String, timeZone: String): ProvidersAvailabilityDto
    suspend fun getAppointments(): AppointmentsDto
}