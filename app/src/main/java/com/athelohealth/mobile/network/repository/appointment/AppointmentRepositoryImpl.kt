package com.athelohealth.mobile.network.repository.appointment

import com.athelohealth.mobile.network.api.AppointmentApi
import com.athelohealth.mobile.network.dto.appointment.ProvidersDto
import com.athelohealth.mobile.network.dto.base.PageResponseDto
import com.athelohealth.mobile.network.repository.BaseRepository
import com.athelohealth.mobile.utils.UserManager
import javax.inject.Inject

class AppointmentRepositoryImpl @Inject constructor(userManager: UserManager):
    BaseRepository<AppointmentApi>(AppointmentApi::class.java, userManager), AppointmentRepository {

    override suspend fun loadProviders(): PageResponseDto<ProvidersDto> {
        return service.loadProviders()
    }
}