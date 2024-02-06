package com.athelohealth.mobile.network.repository.appointment

import com.athelohealth.mobile.network.api.AppointmentApi
import com.athelohealth.mobile.network.dto.appointment.AppointmentsDto
import com.athelohealth.mobile.network.dto.appointment.ProvidersAvailabilityDto
import com.athelohealth.mobile.network.dto.appointment.ProvidersDto
import com.athelohealth.mobile.network.repository.BaseRepository
import com.athelohealth.mobile.utils.UserManager
import javax.inject.Inject

class AppointmentRepositoryImpl @Inject constructor(userManager: UserManager):
    BaseRepository<AppointmentApi>(AppointmentApi::class.java, userManager), AppointmentRepository {

    override suspend fun loadProviders(): ProvidersDto {
        return service.loadProviders()
    }

    override suspend fun getProvidersAvailability(date: String): ProvidersAvailabilityDto {
        return service.getProvidersAvailability(date = date)
    }

    override suspend fun getAppointments(): AppointmentsDto {
        return service.getAppointments()
    }
}