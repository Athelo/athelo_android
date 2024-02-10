package com.athelohealth.mobile.network.repository.appointment

import com.athelohealth.mobile.network.api.AppointmentApi
import com.athelohealth.mobile.network.dto.appointment.AppointmentsDto
import com.athelohealth.mobile.network.dto.appointment.BookAppointmentDto
import com.athelohealth.mobile.network.dto.appointment.BookAppointmentRequestDto
import com.athelohealth.mobile.network.dto.appointment.ProvidersAvailabilityDto
import com.athelohealth.mobile.network.dto.appointment.ProvidersDto
import com.athelohealth.mobile.network.repository.BaseRepository
import com.athelohealth.mobile.utils.UserManager
import javax.inject.Inject

class AppointmentRepositoryImpl @Inject constructor(userManager: UserManager) :
    BaseRepository<AppointmentApi>(AppointmentApi::class.java, userManager), AppointmentRepository {

    override suspend fun loadProviders(): ProvidersDto {
        return service.loadProviders()
    }

    override suspend fun getProvidersAvailability(
        id: String,
        date: String,
        timeZone: String
    ): ProvidersAvailabilityDto {
        return service.getProvidersAvailability(id = id, date = date, timeZone = timeZone)
    }

    override suspend fun getAppointments(): AppointmentsDto {
        return service.getAppointments()
    }

    override suspend fun bookAppointment(
        providerId: Int,
        startTime: String,
        endTime: String,
        timeZone: String
    ): BookAppointmentDto {
        return service.bookAppointment(
            body = BookAppointmentRequestDto(
                providerId = providerId,
                startTime = startTime,
                endTime = endTime,
                timeZone = timeZone
            )
        )
    }

    override suspend fun deleteAppointment(id: Int): Boolean {
        return service.deleteAppointment(id = id.toString()).isSuccessful
    }
}